package fi.solita.utils.serialization;


public class EmployeeDto<T> {
	
	public static class Salary {
		public int euros;
	}

	public final int age;
	public final String name;
	public Salary salary = new Salary() {{euros = 69;}};
	public final T something;
	
	public EmployeeDto(int age, String name, T something) {
		this.name = name;
		this.age = age;
		this.something = something;
	}
	
	public Salary getSalary() {
		return salary;
	}
}
