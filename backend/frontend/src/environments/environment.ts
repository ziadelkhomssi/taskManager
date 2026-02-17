import { commonEnvironment } from "./environment.common";

export const environment = {
    ...commonEnvironment,
    API_URL: "http://10.103.8.139:8090/api",
    enableDevelopmentRoutes: false,
};
