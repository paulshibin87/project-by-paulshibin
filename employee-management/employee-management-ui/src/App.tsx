import "./App.css";
import NavBar from "./components/NavBar";
import AddEmployee from "./components/AddEmployee";
import EmployeeList from "./components/EmployeeList";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import UpdateEmployee from "./components/UpdateEmployee";

function App() {
  return (
    <>
      <BrowserRouter>
        <NavBar />
        <Routes>
          <Route index element={<EmployeeList />}></Route>
          <Route path="/" element={<EmployeeList />}></Route>
          <Route path="/employeeList" element={<EmployeeList />}></Route>
          <Route path="/addEmployee" element={<AddEmployee />}></Route>
          <Route path="/editEmployee/:id" element={<UpdateEmployee />}></Route>
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;
