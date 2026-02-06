import { AtsRequest, AtsResponse, CapacitacionRequest, CapacitacionResponse, InspeccionEppRequest, InspeccionEppResponse, InspeccionHerramientaRequest, InspeccionHerramientaResponse, PetarRequest, PetarResponse } from "./ssoma.model";

export interface SsomaCompletoRequest {
  ats?: AtsRequest;
  capacitacion?: CapacitacionRequest;
  inspeccionEpp?: InspeccionEppRequest;
  inspeccionHerramienta?: InspeccionHerramientaRequest;
  petar?: PetarRequest;
}

export interface SsomaCompletoResponse {
  mensaje: string;
  transaccionId?: string;
  ats?: AtsResponse;
  capacitacion?: CapacitacionResponse;
  inspeccionEpp?: InspeccionEppResponse;
  inspeccionHerramienta?: InspeccionHerramientaResponse;
  petar?: PetarResponse;
}
