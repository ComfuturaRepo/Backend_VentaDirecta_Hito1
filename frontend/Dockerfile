# ================================
# 1️⃣ Build Angular
# ================================
FROM node:20-alpine AS build

WORKDIR /app

# Copiamos package.json e install
COPY package*.json ./
RUN npm install

# Copiamos todo el proyecto
COPY . .

# Build de producción
RUN npm run build -- --configuration production

# ================================
# 2️⃣ Servir Angular con Nginx
# ================================
FROM nginx:alpine

# Borramos el default welcome
RUN rm -rf /usr/share/nginx/html/*

# Copiamos build de Angular al html de Nginx
COPY --from=build /app/dist/frontend /usr/share/nginx/html

# Copiamos configuración de Nginx para SPA
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponemos el puerto 80 para Railway
EXPOSE 80

# Ejecutamos Nginx
CMD ["nginx", "-g", "daemon off;"]
