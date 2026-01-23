# ================= BUILD FRONTEND =================
FROM node:20-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

# Build Angular producción
RUN ng build --configuration production

# ================= RUN IN NGINX =================
FROM nginx:alpine

# Limpiar contenido viejo
RUN rm -rf /usr/share/nginx/html/*

# Copiar build de Angular SPA (browser) a Nginx
COPY --from=build /app/dist/frontend/ /usr/share/nginx/html/

# Copiar nginx custom
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
