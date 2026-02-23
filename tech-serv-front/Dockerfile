# Stage 1: Build
FROM node:20 AS build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build || (ls -R src/ && exit 1)

# Stage 2: Production
FROM nginx:stable-alpine
# Copiamos los archivos compilados de Vue
COPY --from=build-stage /app/dist /usr/share/nginx/html
# Copiamos una config básica de nginx para que Vue Router funcione (opcional pero recomendado)
RUN echo 'server { listen 80; location / { root /usr/share/nginx/html; index index.html; try_files $uri $uri/ /index.html; } }' > /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]