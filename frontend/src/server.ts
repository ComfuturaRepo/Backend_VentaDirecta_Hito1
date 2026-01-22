// src/server.ts
import { CommonEngine } from '@angular/ssr/node';
import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';        // ← ajusta el nombre si usas app.config.ts o app.config.server.ts
import { App } from './app/app';

const engine = new CommonEngine();

export default async function render(url: string, document: string) {
  return engine.render({
    bootstrap: () => bootstrapApplication(App, appConfig),
    document,
    url,
  });
}

// Esto es opcional, pero evita warnings en algunos entornos:
export const __ngAppEngineManifest = {};
