package fi.solita.utils.serialization;

import java.util.Set;


public class DepartmentDto {
        public final Set<EmployeeDto<Boolean>> employees;
		public final String name;
		
		public DepartmentDto(Set<EmployeeDto<Boolean>> employees, String name) {
			this.name = name;
			this.employees = employees;
		}

		public int getEmployeeCount() {
			return employees.size();
		}
}
