# Stage 1: Build Angular app
FROM node:22 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build --prod

# Stage 2: Serve with Nginx
FROM nginx:1.27
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html
EXPOSE 80
