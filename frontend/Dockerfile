# ================= BUILD FRONTEND =================
FROM node:20-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build -- --configuration production

# ================= RUN IN NGINX =================
FROM nginx:alpine
WORKDIR /usr/share/nginx/html

# Limpiar contenido viejo
RUN rm -rf ./*

# Copiar build correcto
COPY --from=build /app/dist/frontend/ /usr/share/nginx/html/

# Copiar nginx custom
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
