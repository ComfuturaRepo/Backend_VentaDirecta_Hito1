# ================= BUILD FRONTEND =================
FROM node:20-alpine AS build
WORKDIR /app

# Copiar package.json y package-lock.json
COPY package*.json ./

# Instalar dependencias
RUN npm install

# Instalar Angular CLI globalmente
RUN npm install -g @angular/cli

# Copiar el proyecto
COPY . .

# Build Angular producción
RUN ng build --configuration production

# ================= RUN IN NGINX =================
FROM nginx:alpine

# Limpiar contenido viejo de Nginx
RUN rm -rf /usr/share/nginx/html/*

# Copiar build de Angular al directorio de Nginx
COPY --from=build /app/dist/frontend/ /usr/share/nginx/html/

# Copiar nginx.conf personalizado
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
