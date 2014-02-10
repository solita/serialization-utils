package fi.solita.utils.serialization;

import static fi.solita.utils.functional.Collections.newSet;
import static fi.solita.utils.functional.Functional.size;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fi.solita.utils.functional.Either;
import fi.solita.utils.functional.Function4;
import fi.solita.utils.serialization.Deserializer.Failure;
import fi.solita.utils.serialization.EmployeeDto.Salary;
import fi.solita.utils.serialization.impl.JSONorgImplementation;
import fi.solita.utils.serialization.json.JSON;
import fi.solita.utils.serialization.json.JSONDeserializer;
import fi.solita.utils.serialization.json.JSONSD;
import fi.solita.utils.serialization.json.JSONSerializer;
import fi.solita.utils.serialization.json.JSONStr;

public class SerializationTest {
    
    static final JSON json = new JSONorgImplementation();
    
    // more complex case, with an inline serializer and a custom constructor function.
    static final JSONSD<EmployeeDto<Boolean>> employeeDto = JSONSD.object(
        EmployeeDto_.<Boolean>$Fields(),
        JSONSD.integer,
        JSONSD.string,
        new JSONSD<EmployeeDto.Salary>(new JSONSerializer<EmployeeDto.Salary>() {
            @Override
            public JSONStr serialize(JSON f, Salary s) {
                return JSONSD.integer.serialize(f, s.euros);
            }
        }, new JSONDeserializer<EmployeeDto.Salary>() {
            @Override
            public Salary deserializeOptimistic(final JSON f, final JSONStr s) {
                return new Salary() {{
                    euros = JSONSD.integer.deserialize(f, s).right.get();
                }};
            }
        }),
        JSONSD.bool,
        new Function4<Integer,String,EmployeeDto.Salary,Boolean,EmployeeDto<Boolean>>() {
            @Override
            public EmployeeDto<Boolean> apply(Integer t1, String t2, final Salary t3, Boolean t4) {
                return new EmployeeDto<Boolean>(t1, t2, t4) {
                    public EmployeeDto.Salary getSalary() {
                        return t3;
                    };
                };
            }
        });
    
    // simple, the most common case.
    static final JSONSD<DepartmentDto> departmentDto = JSONSD.object(
        DepartmentDto_.$Fields(),
        JSONSD.set(employeeDto),
        JSONSD.string,
        DepartmentDto_.$);
    
	@Test
	public void serializeDto() {
	    JSONStr s = json.serialize(departmentDto, new DepartmentDto(newSet(new EmployeeDto<Boolean>(42, "emp", true)), "dep"));
		assertEquals(json.serialize(departmentDto, json.deserialize(departmentDto, s).right.get()), s);
	}
	
	@Test
	public void deserializeDto() {
	    DepartmentDto dto = new DepartmentDto(newSet(new EmployeeDto<Boolean>(42, "emp", true)), "dep");
        assertEquals(json.serialize(departmentDto, dto), json.serialize(departmentDto, json.deserialize(departmentDto, json.serialize(departmentDto, dto)).right.get()));
    }
	
	@Test
    public void jsonSyntaxFailure() {
        Either<Failure<DepartmentDto>, DepartmentDto> res = json.deserialize(departmentDto, new JSONStr("{'name': 'foo', 'employees': [{'a':1}}"));
        assertTrue(res.isLeft());
        assertEquals(1, size(res.left.get().errors));
    }
	
	@Test
    public void contentFailure() {
        Either<Failure<DepartmentDto>, DepartmentDto> res = json.deserialize(departmentDto, new JSONStr("{'name': 'foo', 'employees': [{'a':1}]}"));
        assertTrue(res.isLeft());
        assertEquals("foo", res.left.get().partialResult.name);
        assertEquals(newSet(null, null, null), res.left.get().partialResult.employees);
        assertEquals(4+1, size(res.left.get().errors));
    }
}
