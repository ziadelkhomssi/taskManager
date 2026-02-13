import { commonEnvironment } from "./environment.common";

export const environment = {
    ...commonEnvironment,
    API_URL: "http://localhost:8090/api",
    enableDevelopmentRoutes: false,
};
