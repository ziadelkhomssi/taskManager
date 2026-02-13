import { commonEnvironment } from "./environment.common";

export const environment = {
    ...commonEnvironment,
    API_URL: "http://localhost:8080/api",
    enableDevelopmentRoutes: true,
};
