import axios from "axios";
import { employeeInterface } from "../interface";

const EMPLOYEE_API_BASE_URL = "http://localhost:8080/api/v1/employees";

class EmployeeService {
  saveEmployee(employee: employeeInterface) {
    return axios.post(EMPLOYEE_API_BASE_URL, employee);
  }

  getEmployees() {
    return axios.get(EMPLOYEE_API_BASE_URL);
  }

  deleteEmployee(id: number) {
    return axios.delete(EMPLOYEE_API_BASE_URL + "/" + id);
  }

  getEmployeeById(id: number) {
    return axios.get(EMPLOYEE_API_BASE_URL + "/" + id);
  }
  updateEmployee(id: number, employee: employeeInterface) {
    return axios.put(EMPLOYEE_API_BASE_URL + "/" + id, employee);
  }
}

export const employeeServiceObj = new EmployeeService();
