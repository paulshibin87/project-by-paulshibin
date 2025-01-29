import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { employeeInterface } from "../interface";
import { employeeServiceObj } from "../services/EmployeeService";

const UpdateEmployee = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [employee, setEmployee] = useState<employeeInterface>({
    firstName: "",
    lastName: "",
    emailId: "",
  });

  useEffect(() => {
    if (id) {
      employeeServiceObj
        .getEmployeeById(parseInt(id))
        .then((res) => {
          setEmployee(res.data);
        })
        .catch((err) => console.log(err));
    }
  }, [id]);

  const updateEmployee = (
    e: React.MouseEvent<HTMLButtonElement, MouseEvent>
  ) => {
    e.preventDefault();
    if (id) {
      employeeServiceObj
        .updateEmployee(parseInt(id), employee)
        .then((res) => navigate("/employeeList"))
        .catch((err) => console.log(err));
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const name = e.target.name;
    const value = e.target.value;

    setEmployee((prevEmp) => {
      return { ...prevEmp, [name]: value };
    });
  };
  return (
    <div className="flex max-w-2xl mx-auto shadow border-b">
      <div className="px-8 py-8">
        <div className="font-thin text-2xl tracking-wider">
          <h1>Update Employee</h1>
        </div>
        <div className="items-center justify-center h-14 w-full my-4">
          <label className="block text-gray-600 text-sm font-normal">
            First Name
          </label>
          <input
            type="text"
            name="firstName"
            value={employee.firstName}
            onChange={(e) => handleChange(e)}
            className="h-10 w-96 border mt-2 px-2 py- 2"
          />
        </div>
        <div className="items-center justify-center h-14 w-full my-4">
          <label className="block text-gray-600 text-sm font-normal">
            Last Name
          </label>
          <input
            type="text"
            name="lastName"
            value={employee.lastName}
            onChange={(e) => handleChange(e)}
            className="h-10 w-96 border mt-2 px-2 py- 2"
          />
        </div>
        <div className="items-center justify-center h-14 w-full my-4">
          <label className="block text-gray-600 text-sm font-normal">
            Email
          </label>
          <input
            type="email"
            name="emailId"
            value={employee.emailId}
            onChange={(e) => handleChange(e)}
            className="h-10 w-96 border mt-2 px-2 py- 2"
          />
        </div>
        <div className="items-center justify-center h-14 w-full my-4 space-x-4 pt-4">
          <button
            onClick={(e) => updateEmployee(e)}
            className="rounded text-white font-semibold bg-green-400 px-6 py-3 hover:bg-green-700"
          >
            Update
          </button>
          <button
            onClick={() => navigate("/employeeList")}
            className="rounded text-white font-semibold bg-red-400 px-6 py-3 hover:bg-red-700"
          >
            Cancle
          </button>
        </div>
      </div>
    </div>
  );
};

export default UpdateEmployee;
