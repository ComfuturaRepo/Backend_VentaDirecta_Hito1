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

# Copiamos build al Nginx html
COPY --from=build /app/dist/frontend /usr/share/nginx/html

# Configuración de SPA para Angular
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponemos el puerto 80 para Railway
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
