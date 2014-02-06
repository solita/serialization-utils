package fi.solita.utils.serialization;

import static fi.solita.utils.functional.Collections.newSet;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fi.solita.utils.functional.Function4;
import fi.solita.utils.serialization.EmployeeDto.Salary;
import fi.solita.utils.serialization.json.JSON;
import fi.solita.utils.serialization.json.JSONDeserializer;
import fi.solita.utils.serialization.json.JSONSD;
import fi.solita.utils.serialization.json.JSONSerializer;
import fi.solita.utils.serialization.json.JSONStr;
import fi.solita.utils.serialization.json.JSONorgImplementation;

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
            public Salary deserialize(final JSON f, final JSONStr s) {
                return new Salary() {{
                    euros = JSONSD.integer.deserialize(f, s);
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
		assertEquals(json.serialize(departmentDto, json.deserialize(departmentDto, s)), s);
	}
	
	@Test
	public void deserializeDto() {
	    DepartmentDto dto = new DepartmentDto(newSet(new EmployeeDto<Boolean>(42, "emp", true)), "dep");
        assertEquals(json.serialize(departmentDto, dto), json.serialize(departmentDto, json.deserialize(departmentDto, json.serialize(departmentDto, dto))));
    }
}
