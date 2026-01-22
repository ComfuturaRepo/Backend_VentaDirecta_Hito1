# ================================
# 1️⃣ Build de Angular
# ================================
FROM node:20-alpine AS build

WORKDIR /app

# Copiamos dependencias
COPY package*.json ./
RUN npm install

# Copiamos el resto del proyecto
COPY . .

# Build en producción (usa el nombre correcto: frontend)
RUN npm run build -- --configuration production

# ================================
# 2️⃣ Imagen final (Node para SSR)
# ================================
FROM node:20-alpine

WORKDIR /app

# Copiamos solo lo necesario del build
COPY --from=build /app/dist ./dist
COPY package*.json ./

# Instalamos solo dependencias necesarias para producción
RUN npm install --omit=dev

# Railway usa la variable PORT
ENV PORT=3000
EXPOSE 3000

# Comando para levantar Angular SSR
CMD ["node", "dist/frontend/server/main.js"]
