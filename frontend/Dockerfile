# ================= BUILD FRONTEND =================
FROM node:20-alpine AS build
WORKDIR /app

# Copiar package.json y package-lock.json
COPY package*.json ./
RUN npm install

# Copiar todo el proyecto
COPY . .

# Build Angular producción
RUN ng build --configuration production

# ================= RUN IN NGINX =================
FROM nginx:alpine
WORKDIR /usr/share/nginx/html

# Limpiar contenido viejo
RUN rm -rf ./*

# Copiar build de Angular SPA (browser) a Nginx
COPY --from=build /app/dist/frontend/ /usr/share/nginx/html/

# Copiar nginx custom
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponer puerto 80
EXPOSE 80

# Ejecutar Nginx en foreground
CMD ["nginx", "-g", "daemon off;"]

#antes que todo
#ng build --configuration production
