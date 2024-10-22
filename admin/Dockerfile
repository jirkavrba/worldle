FROM node:22.10.0-slim AS builder

WORKDIR /builder

COPY package.json /builder/package.json
COPY package-lock.json /builder/package-lock.json

RUN npm install

COPY . /builder

RUN npm run build


FROM nginx:stable-alpine

COPY ./docker/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /builder/dist /var/www/html

EXPOSE 3000

ENTRYPOINT ["nginx", "-g", "daemon off;"]
