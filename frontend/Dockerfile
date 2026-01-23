# ================= BUILD FRONTEND =================
FROM node:20-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm install
RUN npm install -g @angular/cli

COPY . .
RUN ng build --configuration production

# ================= RUN IN NGINX =================
FROM nginx:alpine

# Limpiar contenido viejo
RUN rm -rf /usr/share/nginx/html/*

# Copiar build Angular
COPY --from=build /app/dist/frontend/ /usr/share/nginx/html/

# Copiar configuración Nginx para SPA
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponer puerto 80
EXPOSE 80

# Ejecutar Nginx en foreground
CMD ["nginx", "-g", "daemon off;"]
