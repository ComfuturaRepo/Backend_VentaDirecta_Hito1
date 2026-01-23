# Etapa 1: Build Angular
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci --legacy-peer-deps   # o npm install si no tienes problemas de peers
COPY . .
RUN npm run build -- --configuration production

# Etapa 2: Nginx
FROM nginx:alpine-slim

# Limpia todo lo default
RUN rm -rf /usr/share/nginx/html/*

# Copia el build – prueba ambas líneas y comenta una
# Opción A: si tus archivos están directamente en dist/frontend
COPY --from=build /app/dist/frontend /usr/share/nginx/html

# Opción B: si están en dist/frontend/browser (muy común en Angular 17+)
# COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html

# Tu nginx.conf personalizado (asegúrate que esté en la raíz del proyecto)
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
