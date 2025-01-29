/* eslint-disable jsx-a11y/anchor-is-valid */
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { employeeInterface } from "../interface";
import { employeeServiceObj } from "../services/EmployeeService";

const EmployeeList = () => {
  const navigate = useNavigate();

  const [employees, setEmployees] = useState<employeeInterface[] | []>([]);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const response = await employeeServiceObj.getEmployees();
        setEmployees(response.data);
      } catch (error) {
        console.log(error);
      }
      setLoading(false);
    };
    fetchData();
  }, []);

  const deleteEmployee = (
    e: React.MouseEvent<HTMLAnchorElement, MouseEvent>,
    id: number | undefined
  ) => {
    if (id) {
      employeeServiceObj
        .deleteEmployee(id)
        .then((res) => {
          if (employees.length > 0) {
            setEmployees((prevEmployee: employeeInterface[]) => {
              return prevEmployee.filter(
                (emp: employeeInterface) => emp.id !== id
              );
            });
          }
        })
        .catch((err) => console.log(err));
    }
  };

  const updateEmployee = (
    e: React.MouseEvent<HTMLAnchorElement, MouseEvent>,
    id: number | undefined
  ) => {
    e.preventDefault();
    if (id) {
      navigate(`/editEmployee/${id}`);
    }
  };

  return (
    <div className="container mx-auto my-8 ">
      <div className="h-12">
        <button
          onClick={() => navigate("/addEmployee")}
          className="rounded bg-slate-600 text-white px-6 py-2"
        >
          Add Employee
        </button>
      </div>
      <div className="flex shadow border-b">
        <table className="min-w-full">
          <thead className="bg-gray-50">
            <tr>
              <th className="text-left font-medium text-gray-500 uppercase tracking-wider px-6 py-3">
                First Name
              </th>
              <th className="text-left font-medium text-gray-500 uppercase tracking-wider px-6 py-3">
                Last Name
              </th>
              <th className="text-left font-medium text-gray-500 uppercase tracking-wider px-6 py-3">
                Email ID
              </th>
              <th className="text-right font-medium text-gray-500 uppercase tracking-wider px-6 py-3">
                Actions{" "}
              </th>
            </tr>
          </thead>
          {!loading && (
            <tbody className="bg-white">
              {employees.map((employee: employeeInterface) => (
                <tr key={employee.id}>
                  <td className="text-left px-6 py-4 whitespace-nowrap">
                    <div className="text-sm text-gray-500">
                      {employee.firstName}
                    </div>
                  </td>
                  <td className="text-left px-6 py-4 whitespace-nowrap">
                    <div className="text-sm text-gray-500">
                      {employee.lastName}
                    </div>
                  </td>
                  <td className="text-left px-6 py-4 whitespace-nowrap">
                    <div className="text-sm text-gray-500">
                      {employee.emailId}
                    </div>
                  </td>
                  <td className="text-right px-6 py-4 whitespace-nowrap font-medium text-sm">
                    <a
                      onClick={(e) => updateEmployee(e, employee?.id)}
                      className="text-indigo-600 hover:text-indigo-800 px-4 hover:cursor-pointer"
                    >
                      Edit
                    </a>
                    <a
                      onClick={(e) => deleteEmployee(e, employee?.id)}
                      className="text-indigo-600 hover:text-indigo-800 hover:cursor-pointer"
                    >
                      Delete
                    </a>
                  </td>
                </tr>
              ))}
            </tbody>
          )}
        </table>
      </div>
    </div>
  );
};

export default EmployeeList;
