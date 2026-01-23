# Etapa 1: Build
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build -- --configuration production

# Etapa 2: Nginx
FROM nginx:alpine-slim
COPY --from=build /app/dist/frontend /usr/share/nginx/html
# Si está en /browser → usa: /app/dist/frontend/browser

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
