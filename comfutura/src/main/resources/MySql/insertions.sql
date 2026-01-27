-- =====================================================
-- INSERCIONES COMPLETAS - ADAPTADAS AL ESQUEMA ACTUAL (sin ots_trabajador)
-- Orden estricto para evitar errores de FK
-- =====================================================

INSERT INTO comfutura.empresa (id_empresa, nombre, ruc, direccion, activo) VALUES
                                                                               (
                                                                                   1,
                                                                                   'COMFUTURA',
                                                                                   '20516285517',
                                                                                   'Dirección Legal: Cal. Hector Arellano Nro. 125, Urbanización México, Distrito: La Victoria, Departamento: Lima, Perú',
                                                                                   1
                                                                               ),
                                                                               (
                                                                                   2,
                                                                                   'GAB',
                                                                                   '20609573164',
                                                                                   'Dirección Legal: Av. Nicolás Arriola Nro. 848, Urbanización Santa Catalina, Distrito: La Victoria, Departamento: Lima, Perú',
                                                                                   1
                                                                               ),
                                                                               (
                                                                                   3,
                                                                                   'ACAPA',
                                                                                   '20574613818',
                                                                                   'Dirección Legal: Cal. Hector Arellano Nro. 125 (a una cuadra del Mercado La Pólvora), Distrito: La Victoria, Departamento: Lima, Perú',
                                                                                   1
                                                                               ),
                                                                               (
                                                                                   4,
                                                                                   'SUDCOM',
                                                                                   '20603078986',
                                                                                   'Dirección Legal: Av. Brasil Nro. 3825 Dpto. 1801, Distrito: Magdalena del Mar, Departamento: Lima, Perú',
                                                                                   1
                                                                               );

-- 2. Clientes
INSERT INTO cliente (razon_social, ruc) VALUES
                                            ('ACAPA ANDINA AGROMIN S.A.C.', '20574613818'),
                                            ('AMERICA MOVIL PERU S.A.C.', '20467534025'),
                                            ('CALA SERVICIOS INTEGRALES S.A.C.', '20606544937'),
                                            ('CARRIER & ENTERPRISE NETWORK SOLUTIONS S.A.C.', '20603657862'),
                                            ('CONSORCIO GAB ELECTRIFICACIÓN', '20613884531'),
                                            ('CONSORCIO IOSSAC III', '20610903411'),
                                            ('DESARROLLOS TERRESTRES PERU S.A.', '20549575308'),
                                            ('GAB', '20606206284'),
                                            ('GYGA CONSULTING S.A.C.', '20600849671'),
                                            ('INGETEC CONSULTORES & EJECUTORES S.R.L. – INGETEC C & E S.R.L.', '20525083552'),
                                            ('MANPOWER PERU S.A.', '20304289512'),
                                            ('MEASURING ENGINEER GROUP PERU S.A.C.', '20505920067'),
                                            ('SITES DEL PERU S.A.C.', '20607207152'),
                                            ('SOLUCIONES TECNOLOGICAS LATINOAMERICA S.A.C.', '20600726219'),
                                            ('SUDCOM GROUP S.A.C.', '20603078986'),
                                            ('COMFUTURA', '20516285517'),
                                            ('CLARO', '20467534026'),
                                            ('ENTEL', '20492917011');

-- 3. Niveles
INSERT INTO nivel (codigo, nombre, descripcion) VALUES
                                                    ('L1', 'Gerencia General', 'Gerencia General / Dirección'),
                                                    ('L2', 'Gerencia / Subgerencia', 'Gerencia funcional y subgerencias'),
                                                    ('L3', 'Jefatura', 'Jefatura / Supervisión Senior'),
                                                    ('L4', 'Coordinación', 'Coordinación / Supervisión operativa'),
                                                    ('L5', 'Operativo', 'Ejecución / Técnicos / Asistentes');
-- 4. Áreas
INSERT INTO area (nombre) VALUES
                              ('RRHH'),
                              ('COSTOS'),
                              ('ENERGIA'),
                              ('CW'),
                              ('COMERCIAL'),
                              ('PEXT'),
                              ('ADMINISTRATIVA'),
                              ('SAQ'),
                              ('ENTEL'),
                              ('SSOMA'),
                              ('ADMIN'),
                              ('TI'),
                              ('CIERRE'),
                              ('CONTABILIDAD'),
                              ('LOGÍSTICA'),
                              ('GERENCIA GENERAL'),
                              ('FINANZAS'),
                              ('CW-ENERGIA'),
                              ('LIMPIEZA');

-- Insertar relaciones cliente-área
INSERT INTO cliente_area (id_cliente, id_area) VALUES
-- COMFUTURA (SUDCOM GROUP S.A.C.)
((SELECT id_cliente FROM cliente WHERE razon_social = 'SUDCOM GROUP S.A.C.'),
 (SELECT id_area FROM area WHERE nombre = 'ADMINISTRATIVA')),

((SELECT id_cliente FROM cliente WHERE razon_social = 'SUDCOM GROUP S.A.C.'),
 (SELECT id_area FROM area WHERE nombre = 'COMERCIAL')),

((SELECT id_cliente FROM cliente WHERE razon_social = 'SUDCOM GROUP S.A.C.'),
 (SELECT id_area FROM area WHERE nombre = 'CONTABILIDAD')),

((SELECT id_cliente FROM cliente WHERE razon_social = 'SUDCOM GROUP S.A.C.'),
 (SELECT id_area FROM area WHERE nombre = 'FINANZAS')),

((SELECT id_cliente FROM cliente WHERE razon_social = 'SUDCOM GROUP S.A.C.'),
 (SELECT id_area FROM area WHERE nombre = 'GERENCIA GENERAL')),

((SELECT id_cliente FROM cliente WHERE razon_social = 'SUDCOM GROUP S.A.C.'),
 (SELECT id_area FROM area WHERE nombre = 'LOGÍSTICA')),

((SELECT id_cliente FROM cliente WHERE razon_social = 'SUDCOM GROUP S.A.C.'),
 (SELECT id_area FROM area WHERE nombre = 'RRHH')),

((SELECT id_cliente FROM cliente WHERE razon_social = 'SUDCOM GROUP S.A.C.'),
 (SELECT id_area FROM area WHERE nombre = 'SSOMA')),

((SELECT id_cliente FROM cliente WHERE razon_social = 'SUDCOM GROUP S.A.C.'),
 (SELECT id_area FROM area WHERE nombre = 'ENERGIA')),

-- CLARO (AMERICA MOVIL PERU S.A.C.)
((SELECT id_cliente FROM cliente WHERE razon_social = 'AMERICA MOVIL PERU S.A.C.'),
 (SELECT id_area FROM area WHERE nombre = 'CW')),

((SELECT id_cliente FROM cliente WHERE razon_social = 'AMERICA MOVIL PERU S.A.C.'),
 (SELECT id_area FROM area WHERE nombre = 'ENERGIA')),


((SELECT id_cliente FROM cliente WHERE razon_social = 'AMERICA MOVIL PERU S.A.C.'),
 (SELECT id_area FROM area WHERE nombre = 'PEXT')),

((SELECT id_cliente FROM cliente WHERE razon_social = 'AMERICA MOVIL PERU S.A.C.'),
 (SELECT id_area FROM area WHERE nombre = 'SAQ')),

((SELECT id_cliente FROM cliente WHERE razon_social = 'AMERICA MOVIL PERU S.A.C.'),
 (SELECT id_area FROM area WHERE nombre = 'TI')),

((SELECT id_cliente FROM cliente WHERE razon_social = 'ENTEL'),
 (SELECT id_area FROM area WHERE nombre = 'CW')),

((SELECT id_cliente FROM cliente WHERE razon_social = 'ENTEL'),
 (SELECT id_area FROM area WHERE nombre = 'ENERGIA')),

((SELECT id_cliente FROM cliente WHERE razon_social = 'ENTEL'),
 (SELECT id_area FROM area WHERE nombre = 'ENTEL')),

((SELECT id_cliente FROM cliente WHERE razon_social = 'ENTEL'),
 (SELECT id_area FROM area WHERE nombre = 'PEXT')),

((SELECT id_cliente FROM cliente WHERE razon_social = 'ENTEL'),
 (SELECT id_area FROM area WHERE nombre = 'TI')),

-- GYGA (GYGA CONSULTING S.A.C.)
((SELECT id_cliente FROM cliente WHERE razon_social = 'GYGA CONSULTING S.A.C.'),
 (SELECT id_area FROM area WHERE nombre = 'PEXT')),

-- SDP (SITES DEL PERU S.A.C.)
((SELECT id_cliente FROM cliente WHERE razon_social = 'SITES DEL PERU S.A.C.'),
 (SELECT id_area FROM area WHERE nombre = 'ENERGIA')),

-- STL (SOLUCIONES TECNOLOGICAS LATINOAMERICA S.A.C.)
((SELECT id_cliente FROM cliente WHERE razon_social = 'SOLUCIONES TECNOLOGICAS LATINOAMERICA S.A.C.'),
 (SELECT id_area FROM area WHERE nombre = 'CW')),

((SELECT id_cliente FROM cliente WHERE razon_social = 'SOLUCIONES TECNOLOGICAS LATINOAMERICA S.A.C.'),
 (SELECT id_area FROM area WHERE nombre = 'ENERGIA')),

((SELECT id_cliente FROM cliente WHERE razon_social = 'SOLUCIONES TECNOLOGICAS LATINOAMERICA S.A.C.'),
 (SELECT id_area FROM area WHERE nombre = 'PEXT')),

((SELECT id_cliente FROM cliente WHERE razon_social = 'SOLUCIONES TECNOLOGICAS LATINOAMERICA S.A.C.'),
 (SELECT id_area FROM area WHERE nombre = 'TI')),

-- INGETEC (INGETEC CONSULTORES & EJECUTORES S.R.L.)
((SELECT id_cliente FROM cliente WHERE razon_social = 'INGETEC CONSULTORES & EJECUTORES S.R.L. – INGETEC C & E S.R.L.'),
 (SELECT id_area FROM area WHERE nombre = 'PEXT'));


INSERT INTO cargo (nombre, id_nivel) VALUES
                                         ('ASISTENTE DE RRHH', (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('ANALISTA FINANCIERO', (SELECT id_nivel FROM nivel WHERE codigo = 'L2')),
                                         ('ANALISTA DE ENERGIA', (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('SUPERVISORA DE OBRAS CIVILES', (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('GERENTE DE CUENTA COMERCIAL', (SELECT id_nivel FROM nivel WHERE codigo = 'L2')),
                                         ('ANALISTA PEXT', (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('AUXILIAR DE OFICINA - DISCAPACIDAD', (SELECT id_nivel FROM nivel WHERE codigo = 'L5')),
                                         ('COORDINADOR DE INGENIERIA', (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('JEFE LEGAL SAQ', (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('JEFE PEXT PINT', (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('GESTOR DE ACCESOS', (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('COORDINADOR DE ENERGIA', (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('SUPERVISORA DE SSOMA', (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('COORDINADOR TI', (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('GERENTE COMERCIAL', (SELECT id_nivel FROM nivel WHERE codigo = 'L2')),
                                         ('JEFE DE CIERRE Y LIQUIDACIONES', (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('JEFE TI', (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('ASISTENTE DE CONTABILIDAD', (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('SUPERVISOR', (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('COORDINADOR DE CW', (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('ASISTENTE DE CONTRATACIONES PUBLICAS', (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('COORDINADOR LEGAL SAQ', (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('SUPERVISOR DE ENERGIA', (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('CADISTA', (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('COORDINADOR PEXT', (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('SUPERVISOR TI', (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('ASISTENTE LOGISTICO', (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('GERENTE', (SELECT id_nivel FROM nivel WHERE codigo = 'L1')),
                                         ('JEFE PEXT', (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('ANALISTA LOGÍSTICO', (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('CONTADORA', (SELECT id_nivel FROM nivel WHERE codigo = 'L2')),
                                         ('COORDINADOR DE IMPLEMENTACION', (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('CONSERJE - DISCAPACIDAD', (SELECT id_nivel FROM nivel WHERE codigo = 'L5')),
                                         ('JEFA DE FINANZAS', (SELECT id_nivel FROM nivel WHERE codigo = 'L1')),
                                         ('SUB GERENTE', (SELECT id_nivel FROM nivel WHERE codigo = 'L1')),
                                         ('CONDUCTOR', (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('JEFE CW - JEFE DE ENERGIA', (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('JEFE DE LOGÍSTICA', (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('ASISTENTE LOGISTICO ENTEL', (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('PRACTICANTE DE ENERGIA', (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('AUXILIAR DE ALMACEN', (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('JEFE DE CIERRE', (SELECT id_nivel FROM nivel WHERE codigo = 'L2')),
                                         ('SUPERVISOR CW', (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('PROJECT MANAGER', (SELECT id_nivel FROM nivel WHERE codigo = 'L2')),
                                         ('SUPERVISOR PEXT', (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('COORDINADOR PLANTA INTERNA', (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('ENCARGADO DE LIMPIEZA', (SELECT id_nivel FROM nivel WHERE codigo = 'L5')),
                                         ('CONSULTOR EXTERNO', (SELECT id_nivel FROM nivel WHERE codigo = 'L5')),

                                         ('JEFATURA RESPONSABLE', (SELECT id_nivel FROM nivel WHERE codigo = 'L1')),
                                         ('LIQUIDADOR', (SELECT id_nivel FROM nivel WHERE codigo = 'L1')),
                                         ('EJECUTANTE', (SELECT id_nivel FROM nivel WHERE codigo = 'L1')),
                                         ('ANALISTA CONTABLE', (SELECT id_nivel FROM nivel WHERE codigo = 'L1'));
-- 6. Bancos
INSERT INTO banco (nombre) VALUES
                               ('BCP - Banco de Crédito del Perú'),
                               ('Interbank'),
                               ('BBVA'),
                               ('Scotiabank'),
                               ('Banco de la Nación'),
                               ('Banco Pichincha'),
                               ('BanBif'),
                               ('Banco Ripley'),
                               ('Banco Falabella'),
                               ('Banco GNB'),
                               ('Banco Santander Perú'),
                               ('Banco Cencosud'),
                               ('Alfin Banco'),
                               ('Banco Azteca Perú'),
                               ('ICBC Perú Bank'),
                               ('Mibanco'),
                               ('Banco Compartamos'),
                               ('Financiera Crediscotia'),
                               ('Financiera Confianza'),
                               ('Financiera Oh!');

INSERT INTO proveedor (ruc, razon_social, contacto, telefono, correo, id_banco, numero_cuenta, moneda) VALUES
                                                                                                           ('20613922866', 'A & B POWER ELECTRIC SERVICIOS GENERALES EIRL', 'ALVARO CANAZA VILCA', '930286433', 'ALVAROCANAZAVILCA8@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '2157224205083', 'SOLES'),
                                                                                                           ('20461619631', 'A Y L SERVICIOS INDUSTRIALES S.A.C', 'CARLOS ALLEN', NULL, 'CALLEN@AYL-SERVICIOS.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1941943643166', 'SOLES'),
                                                                                                           ('20606172070', 'A1PERU PROYECTOS & ESTRUCTURAS E.I.R.L.', NULL, '980560609', 'contacto@A1peru.com', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0832-0200490995', 'SOLES'),
                                                                                                           ('20605263144', 'AC REFRIGERACION PERU S.A.C.', 'VICTOR DIAZ DEL OLMO', '987106554', 'INFO@AC-REFRIGERACIONPERU.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '193-2635902071', 'SOLES'),
                                                                                                           ('20602117091', 'AFA TOURS PERU S.A.C.', 'JOSE CAMPANA AFATA', '966707225', 'TRANSPORTE@AFATOURSPERU.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-2644342-0-24', 'SOLES'),
                                                                                                           ('20544858751', 'ALARMAS N CUENCA E.I.R.L.', 'Fid_bancoEL TICSE LAVERIANO', NULL, 'alarmasncuenca@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '19476186059013', 'SOLES'),
                                                                                                           ('20610806156', 'ALEMAR ESTRUCTURAS PERU S.A.C.', NULL, NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-9961676-0-87', 'SOLES'),
                                                                                                           ('20603856806', 'ANDAMIOS NORMADOS PERU E.I.R.L.', NULL, NULL, 'jsamuelpg@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '215-76039740-0-13', 'SOLES'),
                                                                                                           ('20602990673', 'ANGHELY INVERSIONES S.A.C.', 'ANGHELO CASTILLO', NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0566-0100012101', 'SOLES'),
                                                                                                           ('20551704441', 'ANJU CORPORACION S.A.C.', 'LUIS YUPANQUI', NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1114100020076180000', 'SOLES'),
                                                                                                           ('20610636650', 'ANTELRED PERU E.I.R.L.', 'MIGUEL BENDEZU MALLMA', '975629202', 'antelred.peru@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0202-0200642854', 'SOLES'),
                                                                                                           ('10451639787', 'APARICIO AMANCA EDGAR ALEXIS', NULL, NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '28534504989089', 'SOLES'),
                                                                                                           ('10442570286', 'APONTE CABALLERO YENSEN', 'YENSEN APONTE CABALLERO', '914249403', 'YENSEN2016@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0257-0200474005', 'SOLES'),
                                                                                                           ('10414110415', 'ARROYO ANAYA DAVid_banco RAUL', 'DAVid_banco A.A', '975397550', 'DAVid_banco.DSIGE@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '200-3003827582', 'DOLARES'),
                                                                                                           ('20600501837', 'ARTE CONSTRUCCIONES Y SERVICIOS GENERALES S.A.C.', 'JESUS SULLCA SALAS', '946897404', 'VENTAS@ACYSG.COM', (SELECT id_banco FROM banco WHERE nombre = 'SCOTIABANK'), '000-2766393', 'SOLES'),
                                                                                                           ('20614450941', 'ARTECH 4.0 S.R.L.', 'JUAN AGÜERO ROSALES', '962889022', 'jaguero.artelecom@outlook.com', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0341-0200488444', 'SOLES'),
                                                                                                           ('20508830603', 'ASENFLO CONSULTORIA Y DESARROLLO DE PROYECTOS S.A.C.', 'JUAN ASENCIOS TRUJILLO', '991730022', 'ASENFLO08@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '1513004067750', 'SOLES'),
                                                                                                           ('20604462011', 'ASESORES & CONSULTORES DESARROLLO EMPRESARIAL S.A.C.', 'MARTIN PAREDES SILVA', '984191256', 'ASESORES.CONSULTORES.CONSTRUCTORES@HOTMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '2003005176970', 'SOLES'),
                                                                                                           ('20609572788', 'B & L CORPORATION E.I.R.L.', 'JOSE RODRIGUEZ', '936074447', 'VPORTA@BYLPERU.PE', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0169-0100055341', 'SOLES'),
                                                                                                           ('20602307949', 'B&H INGENIERIA PROYECTOS Y SOLUCIONES SAC', 'CESAR BANCES SALAZAR', '937719100', 'CESAR.BANCES@BHINPROSOL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '0011-0216-0200167826', 'SOLES'),
                                                                                                           ('20601794226', 'BAV ENERGIA Y CONSTRUCCION S.A.C.', 'RENZO VIZCARRA ZEBALLOS', '992686927', 'RENZOUVZ@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'SCOTIABANK'), '4367880', 'SOLES'),
                                                                                                           ('20613532715', 'BINARY SOLUTIONS S.A.C.', 'ALDO VILLEGAS', NULL, 'BINARY.SOLUTIONS.T@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '300-3006862670', 'SOLES'),
                                                                                                           ('20611765755', 'BLUCOM S.A.C.', 'ERIK REQUEZ', '933166054', 'ERIKREQUEZ@BLUCOM.PE', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1911677320025', 'SOLES'),
                                                                                                           ('10427641924', 'BRAVO VILCHEZ JERRY LUIS', 'JERRY BRAVO VILCHEZ', NULL, 'JERRY_22_BV@HOTMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '219100423855700000', 'SOLES'),
                                                                                                           ('20533660666', 'C & A.H. CONTRATISTAS S.A.C.', 'OSCAR CUEVA ROSAS', '993851772', 'OSCARCUEVAROSAS4@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '6223005960810', 'SOLES'),
                                                                                                           ('20610948350', 'CAMERINO SPORTS E.I.R.L.', NULL, NULL, 'CAMERINOSPORTSS@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '7023005395862', 'SOLES'),
                                                                                                           ('20538137716', 'CARISO CORPORACIONES S.A.C', 'JULISA TRUJILLO', NULL, 'VENTAS1@SAGAMA-INDUSTRIAL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '110484010001778000', 'SOLES'),
                                                                                                           ('20600707842', 'CBTEL PERU S.A.C.', 'WALTER CAMACHO', '941417168', 'WCAMACHO@CBTELPERU.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '200-3004615504', 'SOLES'),
                                                                                                           ('20556084662', 'CDA INGENIEROS DEL PERÚ SAC', 'FANNY', NULL, 'VENTAS@CDA-INGENIEROS.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0312-0100022288-63', 'SOLES'),
                                                                                                           ('20601801338', 'CELCOM INGENIEROS S.A.C.', 'ALFREDO HUAMAN', '978867399', 'VENTAS@CELCOMINGENIEROS.PE', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1919409044047', 'SOLES'),
                                                                                                           ('20601914621', 'CERTEL SOLUCIONES OPTICAS INSTALACIONES Y MANTENIMIENTO S.A.C.', 'JORGE CERVANTES', '979599550', 'CERTEL.SAC@HOTMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1912386919090', 'SOLES'),
                                                                                                           ('20610133674', 'CHANG SERVICES & INVESTMENTS E.I.R.L.', 'FRANK CHANG', NULL, 'CHANG.SERVICES.INVESTMENTS@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '3904197158026', 'SOLES'),
                                                                                                           ('20610857478', 'CHARGE TELECOMUNICACION S.A.C.', 'AGUSTIN CONCHUCOS', '933709208', NULL, (SELECT id_banco FROM banco WHERE nombre = 'SCOTIABANK'), '3593913', 'SOLES'),
                                                                                                           ('20607235831', 'CJR SOLUTIONS ENTERPRISE S.A.C.', 'JHOEL RIVERA', NULL, 'johel.rivera@cjrsolutionsenterprise.com', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0832-0100025055', 'SOLES'),
                                                                                                           ('20609219425', 'CMI CONSULTING IN HUMAN RESOURCES S.A.C.', 'ALEXANDER ALVARADO BAUDAT', '956622934', NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1919898037069', 'SOLES'),
                                                                                                           ('20602964320', 'COEPER PERU S.A.C.', 'YULY', NULL, 'Ventas@coeperper.com', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0346-0100029814-49', 'SOLES'),
                                                                                                           ('20551613217', 'COMPAÑIA MAGRA SAC', 'YULY GAMBINI SUAREZ', '991236688', 'YULYG@MAGRASAC.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-03120100015796', 'SOLES'),
                                                                                                           ('20550048893', 'CONCEPTOS SOCIALES PERU S.A.C.', 'JUAN AGUERO', '962889022', 'JAGUERO.ARTELECOM@OUTLOOK.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '200-3005167513', 'SOLES'),
                                                                                                           ('20100063680', 'CONDUCTORES ELECTRICOS LIMA S A', 'ANA FLORES', NULL, 'AMFLORES@CELSA.COM.PE', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0910-0100001655', 'SOLES'),
                                                                                                           ('20605001832', 'CONELI PERU S.A.C.', NULL, NULL, 'ventas@coneliperu.com', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0346-0100028117', 'SOLES'),
                                                                                                           ('20604773041', 'CONFECCIONES JECI S.A.C.', NULL, NULL, 'CONFECCIONESJECI1@HOTMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '110140100200709000', 'SOLES'),
                                                                                                           ('20448370993', 'CONSORCIO PERUANO INGENIEROS SAC', 'JUVENAL TIPULA MAMANI', '951404000', 'jtipula@cooperingenieros.com', NULL,  '3223003459569', 'SOLES'),
                                                                                                           ('20611765534', 'CONSTRUCCION E IMPLEMENTACION NETWORKS S.A.C.', 'MIGUEL CABALLERO CORDERO', '922320227', 'MCABALLEROC95@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '600-300561270', 'SOLES'),
                                                                                                           ('20494087147', 'CONSTRUCCIONES N&H S.A.C.', 'NICOLAS GARCIA TELLO', '953625610', 'construccionesnh2018@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0310-0100112987', 'SOLES'),
                                                                                                           ('20612650374', 'CONSTRUCTORA Y SERVICIOS GENERALES OC Y MM S.A.C.', 'ERICK PARRA', NULL, 'f.ieparra@groupcorpafinity.com', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '2003006229725', 'SOLES'),
                                                                                                           ('20612254207', 'CONSTRUCTORA Y SERVICIOS Wid_bancoI S.A.C.', 'WILMER DIAZ', '931530495', 'WILMERDIAZ0793@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1914213622037', 'SOLES'),
                                                                                                           ('20610563741', 'CONSTRUCTORES & CONSULTORES AP & RC S.A.C.', 'CELINDA ZAPATA SAAVEDRA', '945736667', 'CELINDA.ZAPATA@APRC.COM.PE', (SELECT id_banco FROM banco WHERE nombre = 'SCOTIABANK'), '3252339', 'SOLES'),
                                                                                                           ('20601460913', 'CORPELIMA S.A.C.', 'LUIS MENDOZA', NULL, 'ventas@corpelima.pe', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-2352920-0-67', 'SOLES'),
                                                                                                           ('20566179068', 'CORPORACION CONTELCOM R & M E.I.R.L.', 'CRISTINA PINEDO', '969420937', 'JOSSELIN.ROLDAN@CONTELCOM.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1912206207018', 'SOLES'),
                                                                                                           ('20524999251', 'CORPORACION ERALD E.I.R.L.', 'ERNESTO ALARCON DIAZ', NULL, 'ALARCONNETO@HOTMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '1101510100040970', 'SOLES'),
                                                                                                           ('20601643902', 'CORPORACION INDUSTRIAL RONNY S.A.C.', 'MARITZA GUERRERO CORCUERA', NULL, 'CORPORACIONINDUSTRIAL2@HOTMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '570-2371374-046', 'SOLES'),
                                                                                                           ('20610643648', 'CORPORACION INTEGRAL H & P PERU S.A.C.', NULL, NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1919939447052', 'SOLES'),
                                                                                                           ('20612518221', 'CORPORACION LENAR E.I.R.L.', NULL, NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1917207057054', 'SOLES'),
                                                                                                           ('20538929736', 'CORPORACION QUIUNTI S.A.C.', 'JEFF CONTRERAS', '944222888', 'jcontreras@quiunti.com', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '2003005426713', 'SOLES'),
                                                                                                           ('20600540018', 'CORPORACION ROME S.A.C.', 'GIAN CALDERON CHAVEZ', NULL, 'GCALDERON@ROME.PE', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1912654904010', 'SOLES'),
                                                                                                           ('20610751033', 'CORPORACION SAMERI S.A.C.', 'YOJAN LLANOS PAICO', '914926274', 'mistiko_2040@hotmail.com', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '2003004945385', 'SOLES'),
                                                                                                           ('20601976782', 'CUSCOBRAS E.I.R.L.', NULL, NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '00-161-311650', 'SOLES'),
                                                                                                           ('20603040318', 'CYCEL E.I.R.L.', 'WILMAN', '963302045', 'CYCEL.INDUSTRIAL@HOTMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1912497216002', 'SOLES'),
                                                                                                           ('20613799371', 'D & E TELECOM S.A.C.', 'EMERSON ENRIQUE', '938192673', 'EMERSON.ENRIQUE@DE-TELECOM.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1917122762089', 'SOLES'),
                                                                                                           ('20548207470', 'DANVEL TELECOM S.A.C.', 'DANIEL HUAMAN', '991054812', 'DANVELTELECOM@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '200-3004402446', 'SOLES'),
                                                                                                           ('20611753617', 'DATTA INGENIERIA S.A.C.', 'CHRISTOFER HUAYANEY VALVERDE', NULL, 'CHRISTOFERHV@DATTAINGENIERIA.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '200-300652168', 'SOLES'),
                                                                                                           ('20523715624', 'DEMERCOM E.I.R.L.', 'JOSE DELGADO', '955481375', 'JOSEDELGADO@DEMERCOM.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '2003005169273', 'SOLES'),
                                                                                                           ('20608652605', 'DESIGN & BUILDING PERU HUANUCO S.A.C.', 'YAMELI URIBE CUYUBAMBA', NULL, 'DESIGNBUILDINGHCO@GMAIL.COM', NULL, '003-561-0030037', 'SOLES'),
                                                                                                           ('20612928160', 'DF ENERGY E.I.R.L.', 'DANIEL FLORES MILLONES', '933731687', 'DANIEL.FLORESMILLONES.2015@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '2157230972036', 'SOLES'),
                                                                                                           ('20607917150', 'DISTRIBUid_bancoOR CABLES ELECTRICOS DEL PERÚ S.A.', 'DIEGO TAPIA', '976335249', 'dicesaperu@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '2459401222084', 'SOLES'),
                                                                                                           ('20603475276', 'DISTRIBUid_bancoORA KRISTELL & LIAN E.I.R.L.', NULL, NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '0011-0566-0200139834', 'SOLES'),
                                                                                                           ('20611100311', 'DRF INSTALLATIONS E.I.R.L.', 'DARWIN REATEGUI', '933695203', 'dreategui@drfinstallations.com', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '200-3005182156', 'SOLES'),
                                                                                                           ('20386659959', 'DUCASSE COMERCIAL S.A.', 'JHONY ABANTO LEON', '932107879', 'JHONY.ABANTO@DUCASSE.COM.PE', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '194-1057803-0-77', 'SOLES'),
                                                                                                           ('20603004761', 'EBREL SOLUCIONES S.A.C', 'CARLOS NUÑEZ', '943090253', 'EBRELSOLUCIONES@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1912508744047', 'SOLES'),
                                                                                                           ('20605047174', 'ECHENIQUE POINT', 'HAROLDO GALO VELA', NULL, 'hgalo@ciaechenique.com', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '7403003296323', 'SOLES'),
                                                                                                           ('20548957126', 'ELECTRONICA ELPOR S.A.C.', 'CLAUDIO PORTAL SIFUENTES', '917659717', 'f.ioperacionesge@groupcorpafinity.com', NULL, '110346020004477000', 'SOLES'),
                                                                                                           ('20554981993', 'EM & J COMUNICACIONES S.R.L.', 'RICHARD COELLO', NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0183-0100150308', 'SOLES'),
                                                                                                           ('20603905581', 'EMERSON DIESEL S.A.C.', 'MIGUEL SOLIS LLANTOY', '940952368', 'RENTA@EMERSONDIESEL.NET', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1942613399068', 'SOLES'),
                                                                                                           ('20610938214', 'ENERGIA POWER S.R.L.', 'JUAN CARLOS VASQUEZ', '999662993', 'JC_VASQUEZROJAS@HOTMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-9413170-0-24', 'SOLES'),
                                                                                                           ('20523719298', 'ENERLAB S.A.C.', 'JOSUE HUAURA MAMANI', '928470015', 'ventas02@enerlab.com.pe', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-1923912-0-53', 'SOLES'),
                                                                                                           ('20545431824', 'ENTELCON S.A.C.', 'GUILLERMO FUENTES', '955640011', 'gfuentes.syl@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '2893002152874', 'SOLES'),
                                                                                                           ('20521044081', 'EQUIPO DE INGENIEROS ESPECIALISTAS S.A.C.', 'JAMES CHASQUIBOL LEON', '912930453', 'jchasquibol@eie-sac.com', (SELECT id_banco FROM banco WHERE nombre = 'BANBIF'), '8025508463', 'SOLES'),
                                                                                                           ('20613253131', 'ESTILO AMBIENTE & SERVICIOS S.A.C.', 'WILLIAM VELASQUEZ PEREZ', '916610117', 'Velasquezperezwilliam@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '19292695453068', 'SOLES'),
                                                                                                           ('20604514372', 'ESTRUCTURAS Y CONCRETOS SELG S.A.C.', 'SEGUNDO LIBAQUE TARRILLO', '955939055', NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '290-2585299-0-21', 'SOLES'),
                                                                                                           ('20610122338', 'EXATEL PERU E.I.R.L.', 'DAVid_banco LLAQUE BARDALES', '949420316', 'dllaque@exatelperu.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '194-9906909-0-85', 'SOLES'),
                                                                                                           ('20551293719', 'FABRICACION Y SUMINISTROS DE TABLEROS ELECTRICOS S.A.C.', 'RAFAEL JIMENEZ', '959179484', 'SEITELINGENIERIA@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1912578742097', 'SOLES'),
                                                                                                           ('20601420008', 'FAMAVE SERVICIOS E.I.R.L.', 'MIGUEL MAZA', '962519440', 'MAILTO:MIGUEL.MAZA@TELKINGPERU.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '4752477369006', 'SOLES'),
                                                                                                           ('20605024913', 'FECOPI E.I.R.L.', 'JENY', NULL, 'FECOPIEIRL@HOTMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0482-0200288914', 'SOLES'),
                                                                                                           ('20607570559', 'FERRELECTRIC G & D S.A.C.', 'BEATRIZ GARIBAY', NULL, 'VENTAS@FERRELECTRICGYD.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-05932762-0-87', 'SOLES'),
                                                                                                           ('20565423968', 'FIBRASTOTAL S.A.C.', 'EFRAIN VELARDE', NULL, 'FIBRASTOTAL@HOTMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-9403838-1-70', 'DOLARES'),
                                                                                                           ('20607842923', 'FLASH TECHNOLOGYS PERU SAC', 'MARBY ROSAS RODRIGUEZ', '922812700', 'MFERNANDO.5191@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '003-600-003005022760-49', 'SOLES'),
                                                                                                           ('20607670731', 'FLN & ASOCIADOS S.R.L.', 'JESUS CARMONA CUSQUISIBAN', '935159374', 'JECARMONAC35@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0814-021606924', 'SOLES'),
                                                                                                           ('20548147710', 'FM & M ESTRUCTURAS S.A.C.', 'MIGUEL VARGAS', '991111582', 'INGENIERIA@FMYMESTRUCTURAS.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0847-0100004874-12', 'SOLES'),
                                                                                                           ('20611515244', 'FULL PACK PERU E.I.R.L.', 'MICHAEL BARBA', '950182138', 'mbarba@fullpackperu.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1917-2264-30041', 'SOLES'),
                                                                                                           ('20607552640', 'FUTURA INGENIERIA Y CONSTRUCCION S.A.C.', 'VICTOR JARA', '917659717', 'F.IOPERACIONESGE@GROUPCORPAFINITY.COM', NULL, '6003006952710', 'SOLES'),
                                                                                                           ('20607120219', 'G & M CONTRATISTAS Y SERVICIOS ELÉCTRICOS GENERALES E.I.R.L.', 'ILDE ALVARADO VERGARAY', '949886294', 'fransua.saenz@hotmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '29029764524046', 'SOLES'),
                                                                                                           ('20610061924', 'G & R SOLUTIONS S.A.C.', 'LENIN GONZALEZ', NULL, 'gyrsolutions2024@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '2053004489060', 'SOLES'),
                                                                                                           ('20610411674', 'GCITEL E.I.R.L.', 'ERNESTO GONZÁLES VILLAR', '900002074', 'GCILTELPERUP@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '4809890926024', 'SOLES'),
                                                                                                           ('20611450886', 'GEOCONTEL INGENIEROS S.A.C.', 'HASSLLEN LEYVA DIAZ', '940738932', 'HLEYVA.GEOCONTEL@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '2003005543760', 'SOLES'),
                                                                                                           ('20552603975', 'GEÒN S.A.C.', 'KADIR FARFAN BEJARANO', NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '1028740', 'SOLES'),
                                                                                                           ('20601996899', 'GLOBAL LCS SERVICIOS GENERALES S.A.C.', 'LUIS CRISPIN', '970 046 928', 'LCRISPIN@LCSGLOBALPERU.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '110155910100045000', 'SOLES'),
                                                                                                           ('20612517160', 'GLOBAL SIGNAL MW E.I.R.L.', 'LEONid_bancoAS LLOCLLA CISNEROS', '963480250', 'george@apinedoglobal.com', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '320000300683524000', 'SOLES'),
                                                                                                           ('20601664179', 'GOLDEN DRAGON CITY S.A.C.', 'ANGEL REYES CRUZ', NULL, 'GOLDENDRAGONSAC@HOTMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0030-0200246377', 'DOLARES'),
                                                                                                           ('20605352589', 'GOLED PERU E.I.R.L.', 'YASMELY CHIPA CARRASCO', NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1918955692033', 'SOLES'),
                                                                                                           ('20600561660', 'GRUAS Y MAQUINARIAS DEL PERU S.A.C.', 'JOEL GAMARRA RENGIFO', NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1931893433084', 'SOLES'),
                                                                                                           ('20610364226', 'GRUPO ALCA COMPANY S.A.C.', 'LUIS MIGUEL HUAMAN', '960817528', 'ventas18@alcacompany.com', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0418-01-00021245', 'SOLES'),
                                                                                                           ('20602159834', 'GRUPO ALEPH S.A.C.', 'EMER Rid_bancoER', '975513161', 'contactos@gpoaleph.com', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0145-0200375857-06', 'SOLES'),
                                                                                                           ('20600721713', 'GRUPO CAAREIN S.A.C.', 'MANUEL TORRES', '938105064', 'ventas@grupocaarein.com.pe', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0178-0100068329', 'SOLES'),
                                                                                                           ('20606470054', 'GRUPO D´PALMA S.A.C.', 'OSCAR PALMA', '995010509', 'LOGISTICA@GRUPODPALMA.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-01890100064773', 'SOLES'),
                                                                                                           ('20212331377', 'GRUPO DELTRON S.A.', 'JOSE BALLENA PAZ', NULL, 'JOSE.BALLENA@DELTRON.COM.PE', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0378-0100014773', 'SOLES'),
                                                                                                           ('20561411540', 'GRUPO JML S.A.C.', 'LUIS SANCHEZ GUEVARA', '917443481', 'grupojmlsac2020@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '3052667088089', 'SOLES'),
                                                                                                           ('20614097583', 'GRUPO KC & V S.A.C.', 'LUIS SANCHEZ GUEVARA', '917443481', 'grupojmlsac2020@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '305-07512176-0-43', 'SOLES'),
                                                                                                           ('20606443154', 'GRUPO QUISPE & MONTENEGRO S.A.C.', 'DENIS QUISPE MONTENEGRO', NULL, 'DQUISPEMONTENEGRO@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '8983419582362', 'SOLES'),
                                                                                                           ('20608741756', 'GRUPO R & O SERVICIOS DE GRUAS Y SOLUCIONES S.A.C.', NULL, NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-93999-570-57', 'SOLES'),
                                                                                                           ('20609474981', 'GRURENTAL PERU S.A.C.', 'ELIO HUAMANI ROJAS', '992771598', 'grurentalperu@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '193-9621104-0-67', 'SOLES'),
                                                                                                           ('20566428033', 'H & L SOLUCIONES S.A.C.', 'HEVER BRICEÑO', '991575593', 'HBRICENO@HLSOLUCIONES.PE', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '1103280100009910', 'SOLES'),
                                                                                                           ('20550825024', 'HEXATEL SAC', 'MARCELO ROCA', NULL, 'MARCELOROCA@HEXATEL.COM.PE', (SELECT id_banco FROM banco WHERE nombre = 'SCOTIABANK'), '1850440', 'SOLES'),
                                                                                                           ('20609580667', 'HK COMPANY S.A.C.', 'LEANDRO MAGALLANES', NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '110857010002659000', 'SOLES'),
                                                                                                           ('20607096890', 'HUCER S.A.C.', 'JORGE CERVANTES', '972874055', NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '19101625577095', 'SOLES'),
                                                                                                           ('20602282989', 'IANASA INVERSIONES SAC', NULL, NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-2429138-0-46', 'SOLES'),
                                                                                                           ('20524139074', 'IC MONTERRA S.A.C.', 'JOSUE MONTERROSO', '900510264', 'JOSUEMONTERROSORAMOS@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '19101567664097', 'SOLES'),
                                                                                                           ('20611251115', 'ILUMINACIONES INDUSTRIALES ROJAS E.I.R.L.', NULL, NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-4218287-0-58', 'SOLES'),
                                                                                                           ('20612034509', 'IMPORTACIONES M3 PERU E.I.R.L.', NULL, NULL, 'VENTAS@M3.COM.PE', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '193-7201594-0-72', 'SOLES'),
                                                                                                           ('20609632756', 'IMPORTACIONES NILDA E.I.R.L.', 'NILDA', NULL, 'nildaelita04@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-7098849042', 'SOLES'),
                                                                                                           ('20547860773', 'IMPORTACIONES RMO SAC', 'RICHARD COELLO HUAMANI', '953431196', 'rcoello@thevasac.com', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '011-346-000200036230', 'SOLES'),
                                                                                                           ('20612605018', 'IMPORTECH PERU S.A.C.', ' JOSE PISFIL CASTAÑEDA', '983475949', 'misilla01@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1944501528078', 'SOLES'),
                                                                                                           ('20251293181', 'INDECO S.A.', 'ORLANDO RIOS', NULL, 'TDAINDUSTRIAL.PERU@NEXANS.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0686-35-0100011892', 'SOLES'),
                                                                                                           ('20609163501', 'INDIGO INVESTMENTS S.A.C.S', 'LISSEL BOHORQUEZ ESPINOZA', NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '420-300615218', 'SOLES'),
                                                                                                           ('20447640598', 'INDUSTRIA E INVERSIONES DEL SUR EIRL', 'RONALD LIVANO OCHOA', NULL, 'INDUSTRIAS2.0@HOTMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '1102290100107750', 'SOLES'),
                                                                                                           ('20307214386', 'INDUSTRIAS MANRIQUE S.A.C.', 'ANGIE VILLALOBOS', NULL, 'ventas@grupomanrique.com', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0349-0100027540-82', 'SOLES'),
                                                                                                           ('20609915626', 'INGTABELEC PERUVIAN S.R.L.', 'ARTURO FUENTES RIVERA', '994000676', 'AFUENTES@INGTABELEC.COM.PE', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0174-0301000686-10', 'SOLES'),
                                                                                                           ('20613380079', 'INNOVA ENERGIA Y CONSTRUCCION S.A.C.', 'JERRY BRAVO VILCHEZ', '960686117', 'JERRY.BRAVO@INGCONST.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1947117498017', 'SOLES'),
                                                                                                           ('20609853094', 'INNOVACION, TECNOLOGIA, ENERGIA Y AMBIENTE - CORPORACION INTEA S.A.C.', 'FRANK Lid_bancoERMAN FUENTES', '970792368', 'REGULACION.AMBIENTAL@CORPORACIONINTEA.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0166-0100070417', 'SOLES'),
                                                                                                           ('20602863078', 'INVERSIONES & SERVICIOS GAMCATEL S.A.C', 'JHON GAMBOA', '906421701', 'jhon.gc@gamcatel.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-2570802-0-95', 'SOLES'),
                                                                                                           ('20606310472', 'INVERSIONES DE TELECOMUNICACIONES J & A S.A.C.', 'JAQUELINE BLANCO', NULL, 'inversionestelcomja20@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BANBIF'), '7000754310', 'SOLES'),
                                                                                                           ('20566369193', 'IRP TELECOM MORABE S.A.C.', 'KATHERINE MORALES', NULL, 'ADMINISTRACION@IRPTELECOM.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '19194160379092', 'SOLES'),
                                                                                                           ('20529816970', 'ITELSAC COMPANY S.R.L.', 'MARTIN ARELLANO MIRANDA', '951403231', NULL, (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0267-24-0100088641', 'SOLES'),
                                                                                                           ('20602053688', 'ITSU PERU E.I.R.L.', 'JORGE ELISBAN SUCNO MAZA', NULL, 'ITSUPERU@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '1102010100041000', 'SOLES'),
                                                                                                           ('20600523822', 'J & M ENERGY SOLUTIONS PERU E.I.R.L.', 'JIMMY MANCHEGO', '990618569', 'PROYECTOS@ENERGYSOLUTIONSPERU.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0319-0100019587-13', 'SOLES'),
                                                                                                           ('20605561790', 'J & N ELECTROINVERSIONES S.A.C.', NULL, NULL, 'jnelectroinversiones@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-2640687-0-04', 'SOLES'),
                                                                                                           ('20522017214', 'J Y H CONTEL S.A.C.', 'ALEXANDER RIOS', '915051697', 'ALEXANDER_RIOSROJAS@YAHOO.ES', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1949909697047', 'SOLES'),
                                                                                                           ('20604511781', 'JADYTEL S.A.C.', 'JAVIER TORIBIO', '966624925', 'jadytelsac@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '194-8758179-0-52', 'SOLES'),
                                                                                                           ('20614665573', 'JAP COMPANY SAC', 'EDGAR APARICIO', NULL, 'aparicioedgaralexis@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '4203007580630', 'SOLES'),
                                                                                                           ('20610786376', 'JBE INGENIERIA & CONSTRUCCION S.A.C.', 'JESUS BARZOLA', '913591615', 'jesus.barzola@jbeingenieria.com', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0081-0200230894', 'SOLES'),
                                                                                                           ('20563629321', 'JOAR TRANSPORTE S.A.C.', 'JOEL ARAUJO VASQUEZ', NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0832-0100006042', 'SOLES'),
                                                                                                           ('20524393701', 'JPS TECNIEXPERTOS E.I.R.L.', 'ANDREINA ROJAS BUENO', '959638465', 'grutecsa2022@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-2237068-0-45', 'SOLES'),
                                                                                                           ('20610534890', 'JT & M TELECOMUNICACIONES DEL NORTE E.I.R.L.', 'JOSE SANTISTEBAN', NULL, 'TELECOMUNICACIONESDELNORTEEIRL@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'SCOTIABANK'), '000-3083552', 'SOLES'),
                                                                                                           ('20509654141', 'KAPEK INTERNACIONAL S.A.C', 'ADRIANA CANALES', '962649491', NULL, (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0486-0100101045', 'DOLARES'),
                                                                                                           ('20610021639', 'L & L PIURA SERVICIOS GENERALES E.I.R.L.', 'SANTOS LIZANA PEÑA', '914254410', 'Santos.lizana@lylpiura.com', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '200-3005167513', 'SOLES'),
                                                                                                           ('10106939514', 'LEON CHILQUILLO MARCELO JUAN', 'MARCELO  LEON CHILQUILLO', '978612915', 'leonmarcelo125@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '19133273296088', 'SOLES'),
                                                                                                           ('20606218762', 'LFT REP S.A.C.', 'HELLEN EGG', NULL, 'VENTAS@LIEFERANT.COM.PE', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-8963489-0-90', 'SOLES'),
                                                                                                           ('20605935614', 'LLATEL PERU S.A.C.', 'CARLA URBINA', '987 779 452', 'llatelperu1@hotmail.com', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '7003003344660', 'SOLES'),
                                                                                                           ('20610822445', 'LOPAN SERVICIOS GENERALES S.A.C.', NULL, NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1919963530015', 'SOLES'),
                                                                                                           ('20610326154', 'LT USA PERU E.I.R.L.', NULL, NULL, 'VENTAS@LTUSAPERU.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0130-0100114345', 'SOLES'),
                                                                                                           ('20608444522', 'MAQSERV JR S.A.C.', 'SANDRA CARRASCO', NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-9560976-0-12', 'SOLES'),
                                                                                                           ('10445292210', 'MARCELO SOSA JULLIANA VANESSA', 'JULLIANA MARCELO SOSA', NULL, 'alvarodamiancastillo@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-08140100656065', 'SOLES'),
                                                                                                           ('20546034951', 'MAVEGSA DRYWALL S.A.C.', 'DAYANNA ALMEid_bancoA RIVERA', '987569205', 'DALMEid_bancoA@MAVEGSA.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1942118695058', 'SOLES'),
                                                                                                           ('20608809768', 'MAXIL INGENIERIA Y SERVICIOS SAC', 'YUVIANA TORRES', '995971219', 'ADMINISTRACION@ARMTELEC.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '2003005167513', 'SOLES'),
                                                                                                           ('10736543708', 'MEDRANO COLLANTES LEONARDO CRISTHIAN', 'LEONARDO MEDRANO COLLANTES', NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0140-0200655720', 'SOLES'),
                                                                                                           ('20609917670', 'METALELECTRIC PERU S.A.C.', 'PERCI VILLALOBOS', NULL, 'PERC.VILLALOBOS@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '2003004556060', 'SOLES'),
                                                                                                           ('20602215947', 'MIRAVAL TELECOMUNICACIONES S.A.C.', 'ROBERT MIRAVAL', NULL, 'MIRATELEC@HOTMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0418-0100014877-13', 'SOLES'),
                                                                                                           ('20610899243', 'MRA SAEZ PERU S.A.C.', 'ALEX  SANCHEZ PEREZ', NULL, 'ALEXSANCHEZPEREZ1991@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0318-0100054352', 'SOLES'),
                                                                                                           ('20550863384', 'MUEBLERIAS OFIMARK PERU S.A.C.', 'CINTIA FORTALEZA', '949571904', NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '194-2055887-0-33', 'SOLES'),
                                                                                                           ('20612212288', 'MULTINEGOCIOS RODAR ELECTRIC S.A.C.', 'ROMARIO', NULL, 'RODARMESCUA@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '440-3005849280', 'SOLES'),
                                                                                                           ('20552370695', 'MULTISERVICE VR S.A.C.', 'WILLIAM PALOMINO', NULL, 'palominowily@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1941444239071', 'SOLES'),
                                                                                                           ('20605134212', 'MULTISERVICIOS AMERICA JD EIRL', 'BERTHA CALDERON AGUIRRE', '943645590', 'VENTAS@AMERICACOM.NET', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '2003005169273', 'SOLES'),
                                                                                                           ('20610758976', 'MULTISERVICIOS CCTEL CUSCO E.I.R.L.', 'EFRAIN CUADROS', NULL, 'cctel.cusco@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '110208020002731000', 'SOLES'),
                                                                                                           ('20490505262', 'MULTISERVICIOS CCTEL SAC', 'CARLOS CUADROS VASQUEZ', '951345920', 'EFRACHO_30@HOTMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '1102000201004350', 'SOLES'),
                                                                                                           ('20607818038', 'MULTISERVICIOS SABAPE E.I.R.L.', 'WILFREDO CABALLERO', '945093760', 'WCABALLEROB@HOTMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '335-03193032-0-02', 'SOLES'),
                                                                                                           ('10749986081', 'NARCIZO VICENTE JHONATAN DANIEL', 'MIGUEL ANGEL AGURTO', '966923424', 'jdnvsba@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0814-0262147814', 'SOLES'),
                                                                                                           ('20613322877', 'NEXGEN INVESTMENT S.A.C.', 'MARCO FERNANDEZ', '944171600', 'NEXGEN.VENTAS@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '2003006521485', 'SOLES'),
                                                                                                           ('20509848697', 'NOVA MEDIC SERVICIOS MEDICOS ESPECIALIZADOS SAC', 'NOVA MEDIC', '940316392', 'comercial@novamedic.org', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0317-01-0002919', 'SOLES'),
                                                                                                           ('20380000122', 'O & E SERVICIOS GENERALES SOCIEDAD DE RESPONSABILid_bancoAD LIMITADA', 'PEDRO GAMARRA MARQUEZ', '998821004', 'frank_leo_81@hotmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-1412380-0-64', 'SOLES'),
                                                                                                           ('20600250834', 'O & M SITEL S.R.L.', 'MARIO OCAMPO', '970941807', NULL, (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0152-0100078675', 'SOLES'),
                                                                                                           ('10456071029', 'OCAÑA MENDOZA KATHIA PAOLA', 'CHRISTIAN  OCAÑA MENDOZA', '935771965', 'PIERREVIANKARODRIGO@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '219410112850010000', 'SOLES'),
                                                                                                           ('20602341586', 'ONE LUX S.A.C.', 'BRAYAN TORRES', '945055614', 'VENTAS@EONELUX.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0939-0100009930', 'SOLES'),
                                                                                                           ('20545087431', 'PARRES S.A.C.', 'GABY', '987362223', 'SDP@PARRES.LAT', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '110189010004279000', 'SOLES'),
                                                                                                           ('20506472343', 'PC BYTE E.I.R.L.', 'ROXANIA SALVADOR CASTILLO', NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-2590047-0-89', 'SOLES'),
                                                                                                           ('20610630457', 'PC GLOBAL PROYECTOS & DESARROLLO INTEGRAL E.I.R.L.', 'CARLA IZAGUIRRE TASAYCO', '904683654', 'PC.GLOBAL.PROYECTOS@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1919937521098', 'SOLES'),
                                                                                                           ('20566247241', 'PGS CONSTRUCTORES S.A.C.', 'PEDRO SANCHEZ', '984311154', 'PGSCONSTRUCTORES.SAC@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '19430121023075', 'SOLES'),
                                                                                                           ('20613252747', 'PIZARRAS A1 VISUAL SHOCK SERVICES E.I.R.L.', 'ERICK ALVA ARCELA', '992858941', 'pizarras.a1@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-7075950-0-40', 'SOLES'),
                                                                                                           ('20530216625', 'PLANTA DE POSTES PIURA SCRL', 'MARTIN ARELLANO MIRANDA', NULL, 'MARELLANO@PPP.COM.PE', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0267-0100098604', 'SOLES'),
                                                                                                           ('20440424792', 'POSTES DEL NORTE S.A.', 'FLORENCIO ALFARO CHAVEZ', '957568030', 'ventas@postesdelnortesa.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '5702614878082', 'SOLES'),
                                                                                                           ('20510077190', 'POSTES ESCARSA S.A.C.', 'MARCO FERNANDEZ OBISPO', '944171600', 'VENTAS4@ESCARSA.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0312-65-0100004212', 'SOLES'),
                                                                                                           ('20511538476', 'POSTES PERU SAC', 'JUNIORS FLORES', NULL, 'POSTESPERU@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0322-0100000084', 'SOLES'),
                                                                                                           ('20610237844', 'PPPERU SERVICIOS GENERALES S.A.C.', 'MARTIN ARELLANO MIRANDA', NULL, 'VENTAS@PPPERU.COM.PE', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '1106670100029850', 'SOLES'),
                                                                                                           ('20602625657', 'PRAXIS CONSULTORIAS & PROYECTOS S.A.C.', 'LUIS PAREDES RAMIREZ', '944410058', 'luisparedes373@yahoo.es', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-9931967-0-96', 'SOLES'),
                                                                                                           ('20610906002', 'PRAXIS PROYECTOS & INVERSIONES E.I.R.L.', 'LUIS PAREDES RAMIREZ', '944410058', 'luisparedes373@yahoo.es', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '219100118504202000', 'SOLES'),
                                                                                                           ('20100084172', 'PROMOTORES ELECTRICOS S A', 'TOMAS HUAMANI LIZANA', NULL, 'THUAMANI@PROMELSA.COM.PE', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0112-01-0001018', 'SOLES'),
                                                                                                           ('20566178410', 'PYJ SOLUTEL E.I.R.L.', 'PEDRO MORALES', NULL, 'PEDRO.MORALES@PYJ-SOLUTEL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1914713709020', 'SOLES'),
                                                                                                           ('20600351754', 'Q & S MEDIACOM S.A.C.', 'FELIX QUISPE RAYMUNDO', '945497527', 'FELIXQR75@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '8534504989089', 'SOLES'),
                                                                                                           ('10715362282', 'QUIROZ LEON YENDER YAMPIERRE', 'YENDER QUIROZ LEON', '990618569', 'Y.LEON.1208@HOTMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0814-0210903882', 'SOLES'),
                                                                                                           ('20601622972', 'R & M INGENIERIA TECNICA S.A.C.', 'ROMELL MARTINEZ', '947331033', 'GERENCIA@RMINGENIERIATECNICA.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0048-0100005087', 'SOLES'),
                                                                                                           ('20602556370', 'R & R ADMINISTRACION Y SERVICIOS E.I.R.L.', NULL, NULL, 'RR-AYS@HOTMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '1102450200332350', 'SOLES'),
                                                                                                           ('20608462351', 'RABHECO DISEÑO & CONSTRUCCIONES S.A.C.', 'RAUL QUISPE', '972261143', 'R.QUISPE@RABHECO.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '200-3004073310', 'SOLES'),
                                                                                                           ('20602568521', 'RB COMMUNICATIONS S.A.C.', 'SAMUEL RAMOS', NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1949842366036', 'SOLES'),
                                                                                                           ('20603746661', 'REDYTEL TI S.R.L.', 'LUIS MAURICIO GUERRA', NULL, 'lmauricio@redytel.pe', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1932525568086', 'SOLES'),
                                                                                                           ('20601307830', 'RENDEL INVERSIONES & SERVICIOS S.A.C.', 'DENIS RENGIFO DEL AGUILA', '982679634', 'DRENGIFO@RENDELSAC.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '200-3001496598', 'SOLES'),
                                                                                                           ('20389372430', 'REPRESENTACIONES MIGAMA S.A.', 'HENRY GARCIA', '955747325', 'HENRY.GARCIA@EMITECSAC.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '200-3005593792', 'SOLES'),
                                                                                                           ('20460360260', 'RESELEC EIRL', 'VICTOR CAMPOS', '999759944', 'VENTASPERU@RESELECPERU.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0116-12-0100025249', 'SOLES'),
                                                                                                           ('20600412923', 'RF TELECOMUNICACIONES Y SEGURid_bancoAD S.A.C.', 'JESUS ROSALES TOMAYLLA', NULL, 'JROSALES@RFTELECOMSE.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0857-0100006480-06', 'SOLES'),
                                                                                                           ('10411969458', 'SANCHEZ CHUGNAS PEDRO GABRIEL', 'PEDRO SANCHEZ CHUGNAS', '984311154', 'PGSCONSTRUCTORESSAC@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '19430121023075', 'SOLES'),
                                                                                                           ('20611173106', 'SC LLAMKAY S.A.C.', 'MILTON SERRANO', '938464986', 'milton.serrano@scllamkay.com', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '1101330100062230', 'SOLES'),
                                                                                                           ('20605473165', 'SEEING CONSULTORIA E INGENIERIA S.A.C.', 'VIANCA JERI', NULL, 'PROYECTOS.SEEING@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '200-3007820591', 'SOLES'),
                                                                                                           ('20544615182', 'SEGEIN PERU S.A.C.', 'EDWAR CHIMANGA', NULL, 'segeinperusac@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '1103380100010660', 'SOLES'),
                                                                                                           ('20612147176', 'SEIPRO E.I.R.L.', 'JOSE QUISPE', '992 197 317', 'jose.quispe@seipro.pe', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '200-3005887168', 'SOLES'),
                                                                                                           ('20544596706', 'SERVICIOS ESPECIALIZADOS DE ASISTENCIA ARQUEOLOGICA S.A.C.', 'GINA MARROU', '992244355', NULL, (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '1931963354057', 'SOLES'),
                                                                                                           ('20613222058', 'SERVICIOS GENERALES & MULTIPLES DEL ORIENTE S.A.C', 'RICHARD RIVERA SAAVEDRA', NULL, 'richardriverasaavedra3@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0281-0200738496', 'SOLES'),
                                                                                                           ('20600645316', 'SERVICIOS GENERALES AITAMI EIRL', 'PERCY ZAPATA', '953985575', 'OPERACIONES@AITAMI.PE', (SELECT id_banco FROM banco WHERE nombre = 'SCOTIABANK'), '7217340', 'SOLES'),
                                                                                                           ('20612494658', 'SERVICIOS GENERALES E IMPLEMENTACION EN TELECOMUNICACIONES E.I.R.L.', 'RAUL ARIAS', NULL, 'RAUL.ARIAS@SEIMETELPERU.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '5303006064811', 'SOLES'),
                                                                                                           ('20610121731', 'SERVICIOS GENERALES MEVI PERU S.A.C.', 'DENIS MEZA VIZCARRA', NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1103670200340160', 'SOLES'),
                                                                                                           ('20601287618', 'SERVISOL INTEGRAL S.A.C.', 'EDGAR ROJAS GUERRA', NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-76727809-0-35', 'SOLES'),
                                                                                                           ('20601262291', 'SGA TELECOMUNICACIONES S.A.C.', 'SAUL GARCIA', '981103466', 'SAUL.GARCIA@SGATEL.COM.PE', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '194-2340483-0-41', 'SOLES'),
                                                                                                           ('20605388222', 'SGM CONSTRUCCIONES S.A.C.', 'FREDDY MONTEBLANCO', NULL, 'freddymonteblanco@hotmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '194-2648543-0-58', 'SOLES'),
                                                                                                           ('20427862331', 'SHERWIN-WILLIAMS PERU S.R.L.', NULL, NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-1138636-0-72', 'SOLES'),
                                                                                                           ('20601248884', 'SILVER TECH COMPANY SAC', 'EDISON LICUONA H.', NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '194-2347974-0-07', 'SOLES'),
                                                                                                           ('20602383360', 'SIMTELECOM PERU S.A.C.', 'GERALDIN CALLEJAS', '943744047', 'GERALDINE.CALLEJAS@SIMTELECOMSAC.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0849-0200263037', 'SOLES'),
                                                                                                           ('20486280876', 'SMART INGENIEROS SAC', 'OTONIEL VILCHEZ GALARZA', '964755168', 'SMARTINGENIEROS@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0002-020018319', 'SOLES'),
                                                                                                           ('20613013556', 'SMTELESOLUTIONS E.I.R.L.', 'JOSE MUCHA', NULL, 'Jmucha@smtelesolutions.com', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '011-135-000201323945', 'SOLES'),
                                                                                                           ('20502381483', 'SOLINT S.R.L.', 'ROLANDO TOMASTO PALOMINO', '999692460', NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1932154056150', 'SOLES'),
                                                                                                           ('20614911931', 'SOLUCIONES ELECTRICAS E INDUSTRIALES RODRIGUEZ S.A.C.', 'JULIO', '944440540', 'VENTA.ELECTRICSOLUTIONS@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '1101720200541880', 'SOLES'),
                                                                                                           ('20600726219', 'SOLUCIONES TECNOLOGICAS LATINOAMERICA S.A.C.', NULL, NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BANBIF'), '7000819162', 'SOLES'),
                                                                                                           ('20611338032', 'SOTELCO GROUP E.I.R.L.', 'DANNY ARAMBURU', '931778553', 'DANY.ARAMBURU@SOTELCOGROUP.NET', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '200-3005584645', 'SOLES'),
                                                                                                           ('20605200169', 'SPT TELECOMUNICACIONES PERU S.A.C.', 'JENNY CALLEJAS GONZALEZ', NULL, 'GERENCIA@SPTELECOMPERU.COM', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '413006948650', 'SOLES'),
                                                                                                           ('20610510079', 'SUCCESS SERVICIOS GENERALES E.I.R.L.', 'YOSVIN ROJAS HERNANDEZ', NULL, 'success.serg23@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-9412479043', 'SOLES'),
                                                                                                           ('10402941818', 'SUCLUPE CARRANZA SANTOS TORIBIO', 'SANTOS SUCLUPE CARRANZA', NULL, 'SANTOSTORIBIO@IMPORTSERVICEALLET.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '011-970-000200404003-12', 'SOLES'),
                                                                                                           ('20610227547', 'SYTELCOM PERU E.I.R.L.', 'JAIME V', NULL, 'ventas@sytelcomperu.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '194-9899209-0-07', 'SOLES'),
                                                                                                           ('20603737866', 'T Y S SALAZAR S.A.C.', 'JUAN BUENDIA', '997510317', 'JBUENDIA_TYS@OUTLOOK.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1932543526080', 'SOLES'),
                                                                                                           ('20607127868', 'TECNOLOGIA Y CONSTRUCCION WARI SAC', 'JESUS HUICHO PRADO', NULL, 'PROYECTOS@TECWARI.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '3558969600076', 'SOLES'),
                                                                                                           ('20303520849', 'TECNOLOGIA Y SERVICIOS CALIFICADOS SAC', NULL, NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'SCOTIABANK'), '240011', 'SOLES'),
                                                                                                           ('20609192349', 'TELCONT PERU S.A.C.', 'BERENICE FERNANDEZ', NULL, 'BFERNANDEZ@TELCONT.COM.PE', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '191-9895483-0-71', 'SOLES'),
                                                                                                           ('20601531187', 'TELECOMUNICACION Y LOGISTICA INTEGRAL PERU S.A.C', 'DANIEL CAPARACHIN', '956580138', 'DANIELCS@INVERSIONESTLI.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '110109640100080000', 'SOLES'),
                                                                                                           ('20604022879', 'TELECOS PERU S.A.C', 'MARCOS BERMUDEZ MOYA', '932070221', 'MARCOS.BERMUDEZ@TELECOS.COM.PE', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '5702609702000', 'SOLES'),
                                                                                                           ('20531829680', 'TELEINSER PERU E.I.R.L.', 'JULIO GONZALES NUÑEZ', NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1912616310073', 'SOLES'),
                                                                                                           ('20600705793', 'TELEMATICA DOBLE FE BIENES Y SERVICIOS S.A.C.', 'OSCAR MENDOZA', NULL, 'OSCAR.MENDOZA@TELEMATICA2FE.PE', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '200-300576057', 'SOLES'),
                                                                                                           ('20602882251', 'TELSYSCOM S.A.C', 'HENRY ROJAS GARCIA', '999626484', 'henryrogar87@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '222000261049809000', 'SOLES'),
                                                                                                           ('20545096693', 'THEVA SAC', 'RICHARD COELLO', NULL, 'RCOELLO@THEVASAC.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1947000768026', 'SOLES'),
                                                                                                           ('20604673535', 'TIES H&A S.A.C.', 'JOSE HOSPINA', '951025292', 'operaciones.ties@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'SCOTIABANK'), '4247785', 'SOLES'),
                                                                                                           ('20608985167', 'TOP SOLUTION METAL S.A.C.', 'ROXANA CARDENAS', NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '1101620200859950', 'SOLES'),
                                                                                                           ('20601457785', 'TRANSLINE IC S.A.C.', 'PAOLO PEREZ', NULL, 'VENTAS@TRANSLINE.PE', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '1932357254045', 'SOLES'),
                                                                                                           ('20603814917', 'TRANSPORTES F&G EXPRESS S.A.C.', 'JAVIER FLORES', '937350906', 'gerencia@transportesfygexpress.com', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '191-87469230-', 'SOLES'),
                                                                                                           ('20611550571', 'TRANSPORTES Y MULTISERVICIOS CUYAS EXPRESS E.I.R.L.', 'PERCY ABANTO', NULL, 'Percyabantogonzales481@gmail.com', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '110323020097548000', 'SOLES'),
                                                                                                           ('20604174938', 'TRANSPORTES Y SERVICIOS MERCAL S.A.C.', 'WILMER DIAZ COTRINA', '910114773', 'DIAZCOTRINA1993@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0619-0100007547', 'SOLES'),
                                                                                                           ('20601628890', 'TUBOS Y POSTES CHACUPE S.A.C.', 'JOSE PISFIL CASTAÑEDA', NULL, 'VENTAS@TUBOSYPOSTESCHACUPE.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0216-01-0001353', 'SOLES'),
                                                                                                           ('20610565272', 'VALENTIN TELECOMUNICACIONES S.A.C.', 'JULIAN VALENTIN QUIJANO', '927721372', 'JULIANVQ2014@GMAIL.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '0011-0310-01-0100147977', 'SOLES'),
                                                                                                           ('20605577947', 'WITLINK S.A.C.', 'CARLOS MALCA ZAPATA', '967945838', 'carlos.malca@witlink.com.pe', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '3058123520064', 'SOLES'),
                                                                                                           ('20609395118', 'WMM E.I.R.L.', 'WALTER', '985246471', NULL, (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '110323020090533000', 'SOLES'),
                                                                                                           ('20607354139', 'WPT SOLUCIONES E.I.R.L.', 'RICHARD COELLO', NULL, NULL, (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '200-3003520305', 'SOLES'),
                                                                                                           ('20555814951', 'YAKU FRESH PERU SAC', 'SHARON PODESTA', NULL, 'VENTAS@YAKUFRESHPERU.COM', (SELECT id_banco FROM banco WHERE nombre = 'BCP'), '194-2155108-0-66', 'SOLES'),
                                                                                                           ('20610035486', 'ZAVCOR ARQUITECTOS S.R.L.', 'TIPHANY FERNANDEZ', '983334830', 'tfernandez@zavcorarquitectos.com', (SELECT id_banco FROM banco WHERE nombre = 'INTERBANK'), '200-300541821', 'SOLES'),
                                                                                                           ('20607535044', 'ZP SOLUCIONES & SERVICIOS E.I.R.L.', 'JORGE ZAVALETA', '936196862', 'JZAVALETA@ZPSOLUCIONESYSERVICIOS.COM', (SELECT id_banco FROM banco WHERE nombre = 'BBVA'), '110752010001996000', 'SOLES');

INSERT INTO proyecto (nombre) VALUES
                                  ('ADECUACION'),
                                  ('AMPLIACION DE POTENCIA'),
                                  ('DENSIFICACION'),
                                  ('DESMONTAJE'),
                                  ('EXPANSION'),
                                  ('IDEOS'),
                                  ('LIMPIEZA 3.5'),
                                  ('MANTENIMIENTO'),
                                  ('OVERLAP'),
                                  ('RANCO'),
                                  ('ROLL OUT'),
                                  ('TRONCAL');


-- 9. Fases
INSERT INTO fase (nombre, orden) VALUES
                                     ('1', 10),
                                     ('2', 20),
                                     ('3', 30),
                                     ('4', 40),
                                     ('5', 50),
                                     ('6', 60),
                                     ('7', 70),
                                     ('8', 80),
                                     ('9', 90),
                                     ('10', 100);

INSERT INTO site (codigo_sitio, descripcion) VALUES
                                                 ('LI0625', 'VOLVO'),
                                                 ('TJ1435', 'NAT_EL_HUARANCHAL'),
                                                 ('TL2410', 'AULAS _USS'),
                                                 ('TL0590', 'NAT_PARQUE_SANTIAGO'),
                                                 ('TL5949', 'NAT_CASA_BLANCA_TUMAN'),
                                                 ('TL6477', 'NAT_AV_LAS_FLORES'),
                                                 ('TP0332', 'NAT_JR_D_MAITE'),
                                                 ('TP0332', 'NAT_VIA_COLECTORA'),
                                                 ('TL2410', 'NAT_EXPLANADA_USS'),
                                                 ('TL2410', 'NAT_PARQUE_ITALIA'),
                                                 ('TL4021', 'NAT_URB_FLEMING'),
                                                 ('TL5949', 'NAT_PAMPA_EL_TORO'),
                                                 ('TJ5160', 'NAT_ALMAGRO'),
                                                 ('TA2227', 'NAT_PAMPAC'),
                                                 ('TP2538', 'NAT_COI_U_PIURA  N'),
                                                 ('TP0038', 'NAT_COI_ENTRADA_PRINCIPAL'),
                                                 ('TP0074', 'NAT_COI_IE_IGNACIO_MERINO'),
                                                 ('TPS417', 'NAT_COI_JOSE_OLAYA'),
                                                 ('TL5977', 'NAT_COI_1108    N'),
                                                 ('TL5573', 'NAT_COI_SAN_JOSE_OBRERO'),
                                                 ('TL5999', 'NAT_COI_EX_COSOME'),
                                                 ('TL5928', 'NAT_COI_JUAN_TOMIS_S'),
                                                 ('TL5993', 'NAT_LLAMPAYEC'),
                                                 ('TL5949', 'NAT_LA_HACIENDA'),
                                                 ('LI4108', 'NAT_HUALCARA'),
                                                 ('0133994', 'AN_Chimbote_Centro_2'),
                                                 ('0134198', 'LM_Ferretero_Paruro'),
                                                 ('0130849', 'IC_Entel_Ica'),
                                                 ('0132207', 'IC_Av_Artemio_Molina'),
                                                 ('0132210', 'IC_Belaunde_Chincha'),
                                                 ('0132290', 'IC_Plaza_Nazca'),
                                                 ('0133515', 'LH_La_Laguna'),
                                                 ('0135172', 'LM_Los_Aguilas'),
                                                 ('0135702', 'LM_Caylloma'),
                                                 ('0130173', 'LM_Zavala'),
                                                 ('0130235', 'LM_Huachipa_Norte'),
                                                 ('0135627', 'IC_Nicolas_Rivera'),
                                                 ('0135627', 'LM_Mercado_Salamanca'),
                                                 ('0135644', 'LM_Tersicore'),
                                                 ('0132231', 'IC_Pasaje_Cilesa'),
                                                 ('0135941', 'LM_Soyuz'),
                                                 ('LI3295', 'ONCE EUCALIPTOS'),
                                                 ('LI3288', 'PARQUE ONTORIO'),
                                                 ('LI3299', 'RESIDENCIAL VENTANILLA'),
                                                 ('LA2829', 'NAT GLORIETA'),
                                                 ('LA2826', 'NAT JOSE OLAYA 2'),
                                                 ('LJ4222', 'NAT_FRANCISCO_CARLE'),
                                                 ('0135322', 'LM_Parque_Los_Pozos'),
                                                 ('0130280', 'LM_Villa_San_Roque'),
                                                 ('0133641', 'HU_Ccochaccasa'),
                                                 ('0132525', 'AQ_Coropuna'),
                                                 ('0130493', 'LM_Mercedarias'),
                                                 ('0130493', 'LM_Bertello'),
                                                 ('0131810', 'TU_Plazuela_Bolognesi'),
                                                 ('0134312', 'LI_Salaverry_Plaza'),
                                                 ('0105840', 'LM_Hacienda_San_Juan'),
                                                 ('0100144', 'LM_Pamplona'),
                                                 ('0134301', 'LM_PS_Hiraoka'),
                                                 ('0134302', 'LM_PS_UCV_SJL'),
                                                 ('0135081', 'LM_PS_Dona_Marcela'),
                                                 ('TP6167', 'NAT_PIURA_P4'),
                                                 ('TT6301', 'NAT_COLON'),
                                                 ('LI0477', 'MIRONES'),
                                                 ('TL0600', 'PUBLIMOVIL BOLOGNESI'),
                                                 ('LI6045', 'NAT PRIMAVERA P2'),
                                                 ('L13230', 'LOS_GRANADOS'),
                                                 ('103082', 'JU_El_Tambo_R1'),
                                                 ('100610', 'LI_El_Porvenir'),
                                                 ('LI1485', 'LAS_LOJAS'),
                                                 ('LI0599', 'POLVO_ROSADO'),
                                                 ('LI3161', 'ZAFIROS_SAN_JUAN'),
                                                 ('LI2442', 'HIEDRA'),
                                                 ('0134562', 'LM_PS_PABLO_CONTI'),
                                                 ('0130077', 'LM_Ugarte_y_Moscoso'),
                                                 ('0131322', 'CS_Espinar'),
                                                 ('0133695', 'CS_Cusco_Antonio'),
                                                 ('013251509', 'LM_PS_Brigada_Especial_R1'),
                                                 ('013250668', 'LM_PS_Fronteras_Unidas'),
                                                 ('LI0692', 'NAT_POLVORITA');

-- 25 Regiones del Perú (24 departamentos + Provincia Constitucional del Callao)
INSERT INTO region (nombre) VALUES
                                ('Amazonas'),
                                ('Áncash'),
                                ('Apurímac'),
                                ('Arequipa'),
                                ('Ayacucho'),
                                ('Cajamarca'),
                                ('Callao'),  -- Provincia Constitucional del Callao
                                ('Cusco'),
                                ('Huancavelica'),
                                ('Huánuco'),
                                ('Ica'),
                                ('Junín'),
                                ('La Libertad'),
                                ('Lambayeque'),
                                ('Lima'),  -- Departamento de Lima (no incluye la provincia de Lima)
                                ('Lima Metropolitana'),  -- Provincia de Lima (especial, aunque técnicamente es provincia)
                                ('Loreto'),
                                ('Madre de Dios'),
                                ('Moquegua'),
                                ('Pasco'),
                                ('Piura'),
                                ('Puno'),
                                ('San Martín'),
                                ('Tacna'),
                                ('Tumbes'),
                                ('Ucayali');

INSERT INTO jefatura_cliente_solicitante (descripcion) VALUES
                                                           ('NC'),
                                                           ('JAVIER'),
                                                           ('OLIVER MASIAS'),
                                                           ('EDWIN HURTADO'),
                                                           ('MAURIICO'),
                                                           ('MAURICIO'),
                                                           ('JOSE ARROYO'),
                                                           ('ERNESTO PAITAN'),
                                                           ('RICARDO BAZAN'),
                                                           ('OMAR EZCURRA'),
                                                           ('JUAN PEÑA'),
                                                           ('PAOLA'),
                                                           ('IGOR ANYOSA'),
                                                           ('OSCAR'),
                                                           ('GUSTAVO CHOCOS');

-- Insertar valores únicos de analista/cliente solicitante
INSERT INTO analista_cliente_solicitante (descripcion) VALUES
                                                           ('COMFUTURA'),
                                                           ('JAVIER'),
                                                           ('OLIVER MASIAS'),
                                                           ('DEMETRIO VEGA'),
                                                           ('DEMETRIO'),
                                                           ('DEIBER'),
                                                           ('JOSE ARROYO'),
                                                           ('ERNESTO PAITAN'),
                                                           ('VLINDA'),
                                                           ('RICARDO BAZAN'),
                                                           ('MAURICIO'),
                                                           ('PAOLA'),
                                                           ('OMAR EZCURRA'),
                                                           ('JUAN PEÑA'),
                                                           ('ANNGIE'),
                                                           ('IGOR ANYOSA'),
                                                           ('OSCAR'),
                                                           ('EDWIN HURTADO'),
                                                           ('PABLO TAMARA'),
                                                           ('ANTHONY'),
                                                           ('GUSTAVO CHOCOS');

INSERT INTO trabajador (nombres, apellidos, dni, celular, correo_corporativo, id_empresa, id_area, id_cargo) VALUES
                                                                                                                 ('WENDY FABIOLA', 'ABARCA MENDIETA', '41493796', '991381184', 'w.abarca@sudcomgroup.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'RRHH'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'ASISTENTE DE RRHH')),

                                                                                                                 ('JUAN RAMON', 'AGUIRRE RONDINEL', '46864991', '933385782', 'saq@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'COSTOS'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'ANALISTA FINANCIERO')),

                                                                                                                 ('JOSE MICHAEL', 'BENAVIDES ROMERO', '74306365', '916374051', 'mbenavides@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ENERGIA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'ANALISTA DE ENERGIA')),

                                                                                                                 ('LEEANN ALEJANDRA', 'BENITES PALOMINO', '75001730', '924919500', 'l.benites@sudcomgroup.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'CW'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'SUPERVISORA DE OBRAS CIVILES')),

                                                                                                                 ('CROSHBI', 'BRICEÑO MARAVI', '41589951', '993903920', 'cbriceno@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'ENTEL'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'COMERCIAL'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'GERENTE DE CUENTA COMERCIAL')),

                                                                                                                 ('ERICK JESUS', 'CABEZAS VILLAR', '70616017', '938989805', NULL,
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'PEXT'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'ANALISTA PEXT')),

                                                                                                                 ('VICTOR HUMBERTO', 'CHAVEZ JUAREZ', '16804166', NULL, NULL,
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ADMINISTRATIVA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'AUXILIAR DE OFICINA - DISCAPACIDAD')),

                                                                                                                 ('JESSICA IVETTE', 'CHIPANA DE LA CRUZ', '46468388', '927020297', 'proyectos3@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'ENTEL'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'CW'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'COORDINADOR DE INGENIERIA')),

                                                                                                                 ('KELLY TATIANA', 'CLEMENTE MARTINEZ', '48010945', '965239660', 'kclementem@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'SAQ'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'JEFE LEGAL SAQ')),

                                                                                                                 ('PEDRO RUDY', 'COLQUE ZATAN', '41026425', '969803234', 'pcolque@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'STL'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'PEXT'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'JEFE PEXT PINT')),

                                                                                                                 ('ERICK GABRIEL', 'CONTRERAS VALLE', '71539602', '969803234', 'econtreras@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'ENTEL'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ENTEL'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'GESTOR DE ACCESOS')),

                                                                                                                 ('NELSON GIOVANNY', 'COSSIO TRUJILLO', '77801770', '967816480', 'c.trujillo@sudcomgroup.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ENERGIA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'COORDINADOR DE ENERGIA')),

                                                                                                                 ('ENNY BELISSA BELEN', 'DE LA CRUZ MAYURI', '42226920', '974075107', 'prevencion@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'SSOMA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'SUPERVISORA DE SSOMA')),

                                                                                                                 ('GUILLERMO CARLOS', 'DIEGUEZ ALZAMORA', '3897788', '986031053', 'gdieguez@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'TI'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'COORDINADOR TI')),

                                                                                                                 ('ALVARO RODRIGO', 'FLORES SAAVEDRA', '42201982', '984177679', 'aflores@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'COMERCIAL'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'GERENTE COMERCIAL')),

                                                                                                                 ('MAGALLY DEL MILAGRO', 'GALINDO LEZAMA', '72315244', '978753064', 'mgalindo@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'CIERRE'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'JEFE DE CIERRE Y LIQUIDACIONES')),

                                                                                                                 ('JOSE CARLOS', 'GONZALEZ MUEDAS', '44373982', '991076898', 'jgonzales@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'TI'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'JEFE TI')),

                                                                                                                 ('ALEXIS MAXIMO', 'GONZALEZ TERRAZOS', '72025774', '982558648', 'asistentecontable@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'CONTABILIDAD'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'ASISTENTE DE CONTABILIDAD')),

                                                                                                                 ('MICHAEL BENYI', 'GRIMALDOS JULCA', '74379867', '970797273', 'ssoma@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'SSOMA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'SUPERVISOR')),

                                                                                                                 ('ERICK MAXIMO', 'GUERRERO ESPINOZA', '9627529', '931031735', 'eguerrero@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'CW'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'COORDINADOR DE CW')),

                                                                                                                 ('SILVIA ROSARIO', 'GUTIERREZ BURGOS', '25728504', '982521285', 's.gutierrez@sudcomgroup.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'COMERCIAL'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'ASISTENTE DE CONTRATACIONES PUBLICAS')),

                                                                                                                 ('HEIDY LISETH', 'HUAMAN CAVIEDES', '44388036', '901837213', 'hhuaman@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'SAQ'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'COORDINADOR LEGAL SAQ')),

                                                                                                                 ('LUIS ENRIQUE', 'LOAYZA LLOCCLLA', '73490407', '9500750970', 'l.loayza@sudcomgroup.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'ENTEL'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ENERGIA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'SUPERVISOR DE ENERGIA')),

                                                                                                                 ('RONALD PABLO', 'LOZANO SIERRA', '47580288', '934591631', 'proyectos1@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'ENTEL'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ENTEL'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'CADISTA')),

                                                                                                                 ('LUIS ANGEL', 'MARTINEZ ESTRADA', '72372882', '949963582', 'pext@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'PEXT'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'COORDINADOR PEXT')),

                                                                                                                 ('JESUS ALONSO', 'MARTINEZ YANQUI', '74919020', '972116383', 'jmartinez@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'TI'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'SUPERVISOR TI')),

                                                                                                                 ('GUILLERMO EDUARDO', 'MASIAS GUERRERO', '40999064', '922958952', 'sistemas@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'LOGÍSTICA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'ASISTENTE LOGISTICO')),

                                                                                                                 ('OLIVIER ANTONIO', 'MASIAS LAGOS', '40264069', '993585214', 'omasias@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'GERENCIA GENERAL'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'GERENTE')),

                                                                                                                 ('ISAAC ROMULO', 'MELENDREZ FERNANDEZ', '6912965', '989181267', 'imelendez@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'PEXT'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'JEFE PEXT')),

                                                                                                                 ('LEONARDO', 'MELGAREJO ALCALA', '72386136', '984106832', 'lmelgarejo@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'LOGÍSTICA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'ANALISTA LOGÍSTICO')),

                                                                                                                 ('ELIZABETH', 'MENDEZ NAVARRO', '6656880', '983276483', 'requerimiento@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'CONTABILIDAD'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'CONTADORA')),

                                                                                                                 ('FRANKLIN', 'MERINO MONDRAGÓN', '45609714', '993546497', 'fmerino@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'ENTEL'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ENTEL'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'COORDINADOR DE IMPLEMENTACION')),

                                                                                                                 ('LUIS ASUNCION', 'MILLONES NECIOSUP', '16514033', NULL, NULL,
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ADMINISTRATIVA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'CONSERJE - DISCAPACIDAD')),

                                                                                                                 ('LOURDES MIRYAM', 'MONTALVAN QUISPE', '70568410', '982174694', 'finanzas@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'FINANZAS'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'JEFA DE FINANZAS')),

                                                                                                                 ('SILVIA ARACELLI', 'NEIRA MATTA', '10145188', '965392681', 'sneira1@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'GERENCIA GENERAL'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'SUB GERENTE')),

                                                                                                                 ('JOSE LUIS', 'NEYRA CORREA', '44875217', '941436602', NULL,
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'LOGÍSTICA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'CONDUCTOR')),

                                                                                                                 ('LUIS FERNANDO', 'ÑIQUEN GOMEZ', '77684556', '921618806', 'lniquen@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'CW-ENERGIA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'JEFE CW - JEFE DE ENERGIA')),

                                                                                                                 ('JORGE MIGUEL', 'OSORIO GALVEZ', '75419660', '994899418', 'josorio@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'TI'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'SUPERVISOR TI')),

                                                                                                                 ('JOSUE MANUEL RAUL', 'OTERO LOJE', '10731488', '987515878', 'jotero@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'LOGÍSTICA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'JEFE DE LOGÍSTICA')),

                                                                                                                 ('SEBASTIAN FELIX', 'OYOLA LOZA', '77812815', '923619930', 'soyola@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'ENTEL'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ENTEL'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'ASISTENTE LOGISTICO ENTEL')),

                                                                                                                 ('JOEL DANIEL', 'PAJUELO LUCIANDO', '78016138', '993440667', NULL,
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ENERGIA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'PRACTICANTE DE ENERGIA')),

                                                                                                                 ('JHAN CARLOS', 'POMACANCHARI QUISPE', '77177882', '983635967', NULL,
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'LOGÍSTICA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'AUXILIAR DE ALMACEN')),

                                                                                                                 ('AIDA LILIANA', 'REYNA CANDELA', '46196532', '997505944', 'areyna@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'CIERRE'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'JEFE DE CIERRE')),

                                                                                                                 ('HENRY JUNIOR', 'RODRIGUEZ YSLACHIN', '75404628', '916629404', 'henry.roys0500@gmail.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'CW'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'SUPERVISOR CW')),

                                                                                                                 ('TOÑO', 'ROMAN QUISPE', '41710568', '910871558', 't.roman@sudcomgroup.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ENERGIA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'COORDINADOR DE ENERGIA')),

                                                                                                                 ('MIGUEL ANGEL', 'SALAS CAMPOS', '32978147', '983276485', 'msalas@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'CW'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'SUPERVISOR CW')),

                                                                                                                 ('BRYAN ANTHONY', 'SALAZAR QUIJAITE', '45820159', '949875742', 'b.salazar@sudcomgroup.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ENERGIA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'SUPERVISOR DE ENERGIA')),

                                                                                                                 ('JOHN DENNIS', 'SANCHEZ ALTAMIRANO', '17635270', '961715063', 'jsanchez@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'TI'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'PROJECT MANAGER')),

                                                                                                                 ('MARY CARMEN', 'SILVA GONZALES', '46580733', '915153318', 'legal3@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'SAQ'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'ASISTENTE DE RRHH')),

                                                                                                                 ('ABEL', 'TAIPE SILVESTRE', '47040061', '983728344', 'abeltaipe3@gmail.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'LOGÍSTICA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'CONDUCTOR')),

                                                                                                                 ('JONATHAN SAUL', 'TINEO ARIAS', '47699858', '976017872', 'jtineo.gab.ing.elec@gmail.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'STL'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'PEXT'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'SUPERVISOR PEXT')),

                                                                                                                 ('ROBER JULIAN', 'VILLARREAL MARCELO', '72901624', '965387686', 'plantainterna@sudcomgroup.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'PEXT'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'COORDINADOR PLANTA INTERNA')),

                                                                                                                 ('ANGELA LISBET', 'ZAMORA FLORES', '76256460', '993635707', 'a.zamora@sudcomgroup.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'CW'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'SUPERVISORA DE OBRAS CIVILES')),

                                                                                                                 ('MARIELA', 'NIEVA ARELLANO', '71244833', '958330870', 'costos@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'CONTABILIDAD'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'ASISTENTE DE CONTABILIDAD')),

                                                                                                                 ('RAFAEL', NULL, NULL, '928542113', NULL,
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'LIMPIEZA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'ENCARGADO DE LIMPIEZA')),

                                                                                                                 ('JESUS OSWALDO', 'BARZOLA MALLMA', '41908627', '913591615', 'jbarzola@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'CLARO'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ENERGIA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'CONSULTOR EXTERNO'));-- 15. Usuarios (ejemplos)
INSERT INTO usuario (username, password, id_trabajador, id_nivel) VALUES
-- Wendy Fabiola Abarca Mendieta
('w.abarca@sudcomgroup.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '41493796'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),

-- Juan Ramon Aguirre Rondinel
('saq@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '46864991'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L2')),

-- Jose Michael Benavides Romero
('mbenavides@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '74306365'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),

-- Leeann Alejandra Benites Palomino
('l.benites@sudcomgroup.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '75001730'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),

-- Croshbi Briceño Maravi
('cbriceno@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '41589951'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L2')),

-- Erick Jesus Cabezas Villar
('ecabezas', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '70616017'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),

-- Victor Humberto Chavez Juarez
('vchavez', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '16804166'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L5')),

-- Jessica Ivette Chipana De La Cruz
('proyectos3@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '46468388'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),

-- Kelly Tatiana Clemente Martinez
('kclementem@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '48010945'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),

-- Pedro Rudy Colque Zatan
('pcolque@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '41026425'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),

-- Erick Gabriel Contreras Valle
('econtreras@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '71539602'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Nelson Giovanny Cossio Trujillo
('c.trujillo@sudcomgroup.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '77801770'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),

-- Enny Belissa Belen De La Cruz Mayuri
('prevencion@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '42226920'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Guillermo Carlos Dieguez Alzamora
('gdieguez@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '3897788'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Alvaro Rodrigo Flores Saavedra
('aflores@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '42201982'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L2')),

-- Magally Del Milagro Galindo Lezama
('mgalindo@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '72315244'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),

-- Jose Carlos Gonzalez Muedas
('jgonzales@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '44373982'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),

-- Alexis Maximo Gonzalez Terrazos
('asistentecontable@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '72025774'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Michael Benyi Grimaldos Julca
('ssoma@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '74379867'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),

-- Erick Maximo Guerrero Espinoza
('eguerrero@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '9627529'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Silvia Rosario Gutierrez Burgos
('s.gutierrez@sudcomgroup.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '25728504'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Heidy Liseth Huaman Caviedes
('hhuaman@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '44388036'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Luis Enrique Loayza LloccLLA
('l.loayza@sudcomgroup.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '73490407'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Ronald Pablo Lozano Sierra
('proyectos1@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '47580288'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Luis Angel Martinez Estrada
('pext@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '72372882'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Jesus Alonso Martinez Yanqui
('jmartinez@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '74919020'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Guillermo Eduardo Masias Guerrero
('sistemas@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '40999064'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Olivier Antonio Masias Lagos
('omasias@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '40264069'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L1')),

-- Isaac Romulo Melendrez Fernandez
('imelendez@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '6912965'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),

-- Leonardo Melgarejo Alcala
('lmelgarejo@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '72386136'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),

-- Elizabeth Mendez Navarro
('requerimiento@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '6656880'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L2')),

-- Franklin Merino Mondragón
('fmerino@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '45609714'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),

-- Luis Asuncion Millones Neciosup
('lmillones', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '16514033'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L5')),

-- Lourdes Miryam Montalvan Quispe
('finanzas@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '70568410'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L1')),

-- Silvia Aracelli Neira Matta
('sneira1@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '10145188'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L1')),

-- Jose Luis Neyra Correa
('jneyra', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '44875217'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Luis Fernando Ñiquen Gomez
('lniquen@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '77684556'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),

-- Jorge Miguel Osorio Galvez
('josorio@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '75419660'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Josue Manuel Raul Otero Loje
('jotero@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '10731488'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),

-- Sebastian Felix Oyola Loza
('soyola@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '77812815'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Joel Daniel Pajuelo Luciando
('jpajuelo', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '78016138'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Jhan Carlos Pomacanchari Quispe
('jpomacanchari', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '77177882'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Aida Liliana Reyna Candela
('areyna@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '46196532'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L2')),

-- Henry Junior Rodriguez Yslachin
('henry.roys0500@gmail.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '75404628'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Toño Roman Quispe
('t.roman@sudcomgroup.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '41710568'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Miguel Angel Salas Campos
('msalas@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '32978147'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),

-- Bryan Anthony Salazar Quijaite
('b.salazar@sudcomgroup.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '45820159'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),

-- John Dennis Sanchez Altamirano
('jsanchez@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '17635270'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L2')),

-- Mary Carmen Silva Gonzales
('legal3@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '46580733'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),

-- Abel Taipe Silvestre
('abeltaipe3@gmail.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '47040061'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Jonathan Saul Tineo Arias
('jtineo.gab.ing.elec@gmail.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '47699858'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Rober Julian Villarreal Marcelo
('plantainterna@sudcomgroup.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '72901624'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Angela Lisbet Zamora Flores
('a.zamora@sudcomgroup.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '76256460'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Mariela Nieva Arellano
('costos@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '71244833'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),

-- Rafael (sin apellido)
('rafael', '123456',
 (SELECT id_trabajador FROM trabajador WHERE nombres = 'RAFAEL' AND apellidos IS NULL),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L5')),

-- Jesus Oswaldo Barzola Mallma
('jbarzola@comfutura.com', '123456',
 (SELECT id_trabajador FROM trabajador WHERE dni = '41908627'),
 (SELECT id_nivel FROM nivel WHERE codigo = 'L5'));
-- 17. Estados de Orden de Compra (sin cambios)
INSERT INTO estado_oc (nombre) VALUES
                                   ('PENDIENTE'), ('APROBADA'), ('RECHAZADA'), ('ANULADA'),
                                   ('EN PROCESO'), ('ATENDIDA'), ('CERRADA');

INSERT INTO unidad_medida (codigo, descripcion) VALUES
                                                    ('UN', 'UNIDADES'),
                                                    ('MT', 'METROS'),
                                                    ('HR', 'HORAS'),
                                                    ('BOR', 'BOR'),
                                                    ('JGO', 'JUEGO'),
                                                    ('BOL', 'BOLSA'),
                                                    ('GR', 'GRAMOS'),
                                                    ('RL', 'ROLLO'),
                                                    ('GL', 'GALON'),
                                                    ('KT', 'KIT'),
                                                    ('PKT', 'PAQUETE'),
                                                    ('KG', 'KILO'),
                                                    ('PR', 'PAR'),
                                                    ('BLD', 'BALDE');


INSERT INTO estado_compra_directa (descripcion) VALUES
                                                    ('REGISTRADO'),
                                                    ('POR APROBACION PRESUPUESTO'),
                                                    ('PRES. APROBADO'),
                                                    ('RECHAZADO'),
                                                    ('ANULADO');-- 13. Estados OT
INSERT INTO estado_ot (descripcion) VALUES
                                        ('ASIGNACION'),
                                        ('PRESUPUESTO ENVIADO'),
                                        ('CREACION DE OC'),
                                        ('EN_EJECUCION'),
                                        ('EN_LIQUIDACION'),
                                        ('EN_FACTURACION'),
                                        ('FINALIZADO'),
                                        ('CANCELADA');