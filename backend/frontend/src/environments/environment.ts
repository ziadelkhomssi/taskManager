import { commonEnvironment } from "./environment.common";

export const environment = {
    ...commonEnvironment,
    API_URL: "https://fasciate-meri-archesporial.ngrok-free.dev/api",
    enableDevelopmentRoutes: false,
};
