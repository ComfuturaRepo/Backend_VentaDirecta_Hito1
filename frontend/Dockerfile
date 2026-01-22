# ================================
# 1️⃣ Build Angular SSR
# ================================
FROM node:20-alpine AS build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

RUN npm run build -- --configuration production

# ================================
# 2️⃣ Runtime (SSR)
# ================================
FROM node:20-alpine

WORKDIR /app

COPY --from=build /app/dist ./dist
COPY package*.json ./

RUN npm install --omit=dev

ENV PORT=3000
EXPOSE 3000

CMD ["node", "dist/frontend/server/server.mjs"]
