import { ReactElement } from "react";
import { render, RenderOptions } from "@testing-library/react";
import UserProvider from "../context/userContext";
import { MemoryRouter } from "react-router-dom";

type CustomRenderOptions = Omit<RenderOptions, "wrapper">;

export const renderWithProvider = (ui: ReactElement,{route = "/",...options}: CustomRenderOptions & { route?: string } = {}) => {
  return render(
    <MemoryRouter initialEntries={[route]}>
      <UserProvider>{ui}</UserProvider>
    </MemoryRouter>,
    options
  );
};