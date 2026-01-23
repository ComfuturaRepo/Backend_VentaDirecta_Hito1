# ================================
# 1️⃣ Build Angular
# ================================
FROM node:20-alpine AS build

# Carpeta de trabajo
WORKDIR /app

# Copiamos package.json e install
COPY package*.json ./
RUN npm install

# Copiamos todo el proyecto
COPY . .

# Construimos Angular para producción
RUN npm run build -- --configuration production

# ================================
# 2️⃣ Servir Angular con Nginx
# ================================
FROM nginx:alpine

# Copiamos los archivos estáticos del build
COPY --from=build /app/dist/frontend /usr/share/nginx/html

# Configuración para Nginx (opcional: evita cache, SPA routing)
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponemos el puerto que Railway va a usar
EXPOSE 80

# Comando para iniciar Nginx
CMD ["nginx", "-g", "daemon off;"]
