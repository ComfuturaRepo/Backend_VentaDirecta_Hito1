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
                                            ('ENTEL PERU S.A', '20492917011');

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
                              ('SISTEMAS'),
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
 (SELECT id_area FROM area WHERE nombre = 'LOGÍSTICA'));




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
                                         ('PROGRAMADOR', (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
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
                                  ('ADMIN'),
                                  ('AMPLIACION DE POTENCIA'),
                                  ('DENSIFICACION'),
                                  ('EXPEDIENTE ELECTRICO'),
                                  ('DESMONTAJE'),
                                  ('EXPANSION'),
                                  ('IDEOS'),
                                  ('LIMPIEZA 3.5'),
                                  ('MANTENIMIENTO'),
                                  ('CRA'),
                                  ('REUBICACION'),
                                  ('OVERLAP'),
                                  ('RANCO'),
                                  ('Autonomia'),
                                  ('ROLL OUT'),
                                  ('ARELLANO'),
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
-- Insertar sitios únicos (eliminando duplicados)
INSERT INTO site (codigo_sitio) VALUES
                                    ('LI0625'),
                                    ('TJ1435'),
                                    ('TL2410'),
                                    ('TL0590'),
                                    ('TL5949'),
                                    ('TL6477'),
                                    ('TP0332'),
                                    ('TL4021'),
                                    ('TJ5160'),
                                    ('TA2227'),
                                    ('TP2538'),
                                    ('TP0038'),
                                    ('TP0074'),
                                    ('TPS417'),
                                    ('TL5977'),
                                    ('TL5573'),
                                    ('TL5999'),
                                    ('TL5928'),
                                    ('TL5993'),
                                    ('LI4108'),
                                    ('0133994'),
                                    ('0134198'),
                                    ('0130849'),
                                    ('0132207'),
                                    ('0132210'),
                                    ('0132290'),
                                    ('0133515'),
                                    ('0135172'),
                                    ('0135702'),
                                    ('0130173'),
                                    ('0130235'),
                                    ('0135627'),
                                    ('0135644'),
                                    ('0132231'),
                                    ('0135941'),
                                    ('LI3295'),
                                    ('LI3288'),
                                    ('LI3299'),
                                    ('LA2829'),
                                    ('LA2826'),
                                    ('LJ4222'),
                                    ('0135322'),
                                    ('0130280'),
                                    ('0133641'),
                                    ('0132525'),
                                    ('0130493'),
                                    ('0131810'),
                                    ('0134312'),
                                    ('0105840'),
                                    ('0100144'),
                                    ('0134301'),
                                    ('0134302'),
                                    ('0135081'),
                                    ('TP6167'),
                                    ('TT6301'),
                                    ('LI0477'),
                                    ('TL0600'),
                                    ('LI6045'),
                                    ('L13230'),
                                    ('0103082'),
                                    ('0100610'),
                                    ('LI1485'),
                                    ('LI0599'),
                                    ('LI3161'),
                                    ('LI2442'),
                                    ('0134562'),
                                    ('0130077'),
                                    ('0131322'),
                                    ('0133695'),
                                    ('013251509'),
                                    ('013250668'),
                                    ('LI0692'),
                                    ('LI1660'),
                                    ('LI0741'),
                                    ('LI1035'),
                                    ('LI0091'),
                                    ('LI0093'),
                                    ('LI4824'),
                                    ('LI5791'),
                                    ('LI5748'),
                                    ('LI5795'),
                                    ('LI5792'),
                                    ('LI4550'),
                                    ('TP6151'),
                                    ('TJ5259'),
                                    ('TJ1449'),
                                    ('TJ6200'),
                                    ('LI4911'),
                                    ('LI6447'),
                                    ('LI2454'),
                                    ('LI2078'),
                                    ('TL2156'),
                                    ('TP2160'),
                                    ('TL2183'),
                                    ('TL2203'),
                                    ('TL2199'),
                                    ('TP2192'),
                                    ('TL2033'),
                                    ('0130543'),
                                    ('0131002'),
                                    ('0131046'),
                                    ('0130086'),
                                    ('0130825'),
                                    ('0135762'),
                                    ('0135860'),
                                    ('0132266'),
                                    ('0132454'),
                                    ('0131022'),
                                    ('0135774'),
                                    ('0135696'),
                                    ('sin_codigo');

INSERT INTO site_descripcion (id_site, descripcion) VALUES
-- Para cada sitio con sus descripciones únicas
((SELECT id_site FROM site WHERE codigo_sitio = 'LI0625'), 'VOLVO'),

((SELECT id_site FROM site WHERE codigo_sitio = 'sin_codigo'), 'GASTOS ADMINISTRATIVOS'),
((SELECT id_site FROM site WHERE codigo_sitio = 'sin_codigo'), 'TRAMITE DOCUMENTARIO - INCREMENTO DE CARGA'),
((SELECT id_site FROM site WHERE codigo_sitio = 'sin_codigo'), 'MODEM COMFUTURA'),

((SELECT id_site FROM site WHERE codigo_sitio = 'TJ1435'), 'NAT_EL_HUARANCHAL'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TL2410'), 'AULAS _USS'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TL2410'), 'NAT_EXPLANADA_USS'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TL2410'), 'NAT_PARQUE_ITALIA'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TL0590'), 'NAT_PARQUE_SANTIAGO'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TL5949'), 'NAT_CASA_BLANCA_TUMAN'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TL5949'), 'NAT_PAMPA_EL_TORO'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TL5949'), 'NAT_LA_HACIENDA'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TL6477'), 'NAT_AV_LAS_FLORES'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TP0332'), 'NAT_JR_D_MAITE'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TP0332'), 'NAT_VIA_COLECTORA'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TL4021'), 'NAT_URB_FLEMING'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TJ5160'), 'NAT_ALMAGRO'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TA2227'), 'NAT_PAMPAC'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TP2538'), 'NAT_COI_U_PIURA  N'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TP0038'), 'NAT_COI_ENTRADA_PRINCIPAL'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TP0074'), 'NAT_COI_IE_IGNACIO_MERINO'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TPS417'), 'NAT_COI_JOSE_OLAYA'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TL5977'), 'NAT_COI_1108    N'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TL5573'), 'NAT_COI_SAN_JOSE_OBRERO'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TL5999'), 'NAT_COI_EX_COSOME'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TL5928'), 'NAT_COI_JUAN_TOMIS_S'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TL5993'), 'NAT_LLAMPAYEC'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI4108'), 'NAT_HUALCARA'),
((SELECT id_site FROM site WHERE codigo_sitio = 'sin_codigo'), 'NAT_LAURA CALLER'),
((SELECT id_site FROM site WHERE codigo_sitio = 'sin_codigo'), 'NAT_TORRE BLANCA'),
((SELECT id_site FROM site WHERE codigo_sitio = '0133994'), 'AN_Chimbote_Centro_2'),
((SELECT id_site FROM site WHERE codigo_sitio = '0134198'), 'LM_Ferretero_Paruro'),
((SELECT id_site FROM site WHERE codigo_sitio = '0130849'), 'IC_Entel_Ica'),
((SELECT id_site FROM site WHERE codigo_sitio = '0132207'), 'IC_Av_Artemio_Molina'),
((SELECT id_site FROM site WHERE codigo_sitio = '0132210'), 'IC_Belaunde_Chincha'),
((SELECT id_site FROM site WHERE codigo_sitio = '0132290'), 'IC_Plaza_Nazca'),
((SELECT id_site FROM site WHERE codigo_sitio = '0133515'), 'LH_La_Laguna'),
((SELECT id_site FROM site WHERE codigo_sitio = '0135172'), 'LM_Los_Aguilas'),
((SELECT id_site FROM site WHERE codigo_sitio = '0135702'), 'LM_Caylloma'),
((SELECT id_site FROM site WHERE codigo_sitio = '0130173'), 'LM_Zavala'),
((SELECT id_site FROM site WHERE codigo_sitio = '0130235'), 'LM_Huachipa_Norte'),
((SELECT id_site FROM site WHERE codigo_sitio = '0135627'), 'IC_Nicolas_Rivera'),
((SELECT id_site FROM site WHERE codigo_sitio = '0135627'), 'LM_Mercado_Salamanca'),
((SELECT id_site FROM site WHERE codigo_sitio = '0135644'), 'LM_Tersicore'),
((SELECT id_site FROM site WHERE codigo_sitio = '0132231'), 'IC_Pasaje_Cilesa'),
((SELECT id_site FROM site WHERE codigo_sitio = '0135941'), 'LM_Soyuz'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI3295'), 'ONCE EUCALIPTOS'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI3288'), 'PARQUE ONTORIO'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI3299'), 'RESIDENCIAL VENTANILLA'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LA2829'), 'NAT GLORIETA'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LA2826'), 'NAT JOSE OLAYA 2'),
((SELECT id_site FROM site WHERE codigo_sitio = 'sin_codigo'), 'NAT_ACHINAMIZA'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LJ4222'), 'NAT_FRANCISCO_CARLE'),
((SELECT id_site FROM site WHERE codigo_sitio = 'sin_codigo'), 'BAJO MARIANKIARI'),
((SELECT id_site FROM site WHERE codigo_sitio = '0135322'), 'LM_Parque_Los_Pozos'),
((SELECT id_site FROM site WHERE codigo_sitio = '0130280'), 'LM_Villa_San_Roque'),
((SELECT id_site FROM site WHERE codigo_sitio = '0133641'), 'HU_Ccochaccasa'),
((SELECT id_site FROM site WHERE codigo_sitio = '0132525'), 'AQ_Coropuna'),
((SELECT id_site FROM site WHERE codigo_sitio = '0130493'), 'LM_Mercedarias'),
((SELECT id_site FROM site WHERE codigo_sitio = '0130493'), 'LM_Bertello'),
((SELECT id_site FROM site WHERE codigo_sitio = '0131810'), 'TU_Plazuela_Bolognesi'),
((SELECT id_site FROM site WHERE codigo_sitio = '0134312'), 'LI_Salaverry_Plaza'),
((SELECT id_site FROM site WHERE codigo_sitio = '0105840'), 'LM_Hacienda_San_Juan'),
((SELECT id_site FROM site WHERE codigo_sitio = '0100144'), 'LM_Pamplona'),
((SELECT id_site FROM site WHERE codigo_sitio = 'sin_codigo'), 'LMSC026-S'),
((SELECT id_site FROM site WHERE codigo_sitio = 'sin_codigo'), 'LMSC018-S'),
((SELECT id_site FROM site WHERE codigo_sitio = 'sin_codigo'), 'LMSC021-S'),
((SELECT id_site FROM site WHERE codigo_sitio = 'sin_codigo'), 'LMSC035-S'),
((SELECT id_site FROM site WHERE codigo_sitio = '0134301'), 'LM_PS_Hiraoka'),
((SELECT id_site FROM site WHERE codigo_sitio = '0134302'), 'LM_PS_UCV_SJL'),
((SELECT id_site FROM site WHERE codigo_sitio = '0135081'), 'LM_PS_Dona_Marcela'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TP6167'), 'NAT_PIURA_P4'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TT6301'), 'NAT_COLON'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI0477'), 'MIRONES'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TL0600'), 'PUBLIMOVIL BOLOGNESI'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI6045'), 'NAT PRIMAVERA P2'),
((SELECT id_site FROM site WHERE codigo_sitio = 'L13230'), 'LOS_GRANADOS'),
((SELECT id_site FROM site WHERE codigo_sitio = '0103082'), 'JU_El_Tambo_R1'),
((SELECT id_site FROM site WHERE codigo_sitio = '0100610'), 'LI_El_Porvenir'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI1485'), 'LAS_LOJAS'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI0599'), 'POLVO_ROSADO'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI3161'), 'ZAFIROS_SAN_JUAN'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI2442'), 'HIEDRA'),
((SELECT id_site FROM site WHERE codigo_sitio = '0134562'), 'LM_PS_PABLO_CONTI'),
((SELECT id_site FROM site WHERE codigo_sitio = '0130077'), 'LM_Ugarte_y_Moscoso'),
((SELECT id_site FROM site WHERE codigo_sitio = '0131322'), 'CS_Espinar'),
((SELECT id_site FROM site WHERE codigo_sitio = '0133695'), 'CS_Cusco_Antonio'),
((SELECT id_site FROM site WHERE codigo_sitio = '013251509'), 'LM_PS_Brigada_Especial_R1'),
((SELECT id_site FROM site WHERE codigo_sitio = '013250668'), 'LM_PS_Fronteras_Unidas'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI0692'), 'NAT_POLVORITA'),
((SELECT id_site FROM site WHERE codigo_sitio = 'sin_codigo'), 'NAT CONDORCANQUI VENT'),
((SELECT id_site FROM site WHERE codigo_sitio = 'sin_codigo'), 'NAT CUMBRE VENT'),
((SELECT id_site FROM site WHERE codigo_sitio = 'sin_codigo'), 'NAT LOS MAESTROS'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI1660'), 'NAT_NUESTRA_SEÑORA_DE_COCHARCAS'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI0741'), 'NAT_ANCASH_JAUJA'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI1035'), 'NAT_CONVENTO_SAN_AGUSTIN'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI0091'), 'NAT_PLAZA_SAN_MARTIN_P1'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI0093'), 'NAT_MINISTERIOS'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI4824'), 'OPEN_PLAZA_P2'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI5791'), 'NAT_RISSO_P2'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI5748'), 'NAT_RESIDENCIAL_P3'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI5748'), 'NAT_RESIDENCIAL_P2'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI5795'), 'NAT_SUCRE_P1'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI5792'), 'NAT_ROOSEVELT_P4'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI5792'), 'NAT_ROOSEVELT_P3'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI5792'), 'NAT_ROOSEVELT_P2'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI4550'), 'NAT_UNIVERSIDADES_P3'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TP6151'), 'NAT_AV_CAHUIDE'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TJ5259'), 'NAT_MERCADO_HUAMAN'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TJ1449'), 'NAT_REFUGIO'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TJ6200'), 'NAT_WICHANZAO'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI4911'), 'NAT_MIYASHIRO_CHORRILLOS'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI6447'), 'NAT_PERCIN_DEZA'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI2454'), 'NAT_NUEVA_LOMA'),
((SELECT id_site FROM site WHERE codigo_sitio = 'LI2078'), 'NAT_LOS_ALPES'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TL2156'), 'CESAR_VALLEJO'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TP2160'), 'SAN_JUAN_DE_BIGOTE'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TL2183'), 'SANTA_ISABEL'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TL2203'), 'A_CACERES'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TL2199'), 'MOCUPE'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TP2192'), 'JR_PIURA'),
((SELECT id_site FROM site WHERE codigo_sitio = 'TL2033'), 'SAN_MARTIN_DE_CHICLAYO'),
((SELECT id_site FROM site WHERE codigo_sitio = '0130543'), 'LM_Repetidor_La_Molina'),
((SELECT id_site FROM site WHERE codigo_sitio = '0131002'), 'LA_Reque'),
((SELECT id_site FROM site WHERE codigo_sitio = '0131046'), 'LA_Puente_Once'),
((SELECT id_site FROM site WHERE codigo_sitio = '0130086'), 'LM_La_Capilla'),
((SELECT id_site FROM site WHERE codigo_sitio = '0130825'), 'IC_Tinguina'),
((SELECT id_site FROM site WHERE codigo_sitio = '0135762'), 'LM_Alto_De_La_Alianza'),
((SELECT id_site FROM site WHERE codigo_sitio = '0135860'), 'LM_Cantabrico'),
((SELECT id_site FROM site WHERE codigo_sitio = '0132266'), 'IC_Pampa_De_La_Isla'),
((SELECT id_site FROM site WHERE codigo_sitio = '0132454'), 'LA_Pacherrez'),
((SELECT id_site FROM site WHERE codigo_sitio = '0131022'), 'LA_Eten'),
((SELECT id_site FROM site WHERE codigo_sitio = '0135774'), 'LM_Lima_Centro'),
((SELECT id_site FROM site WHERE codigo_sitio = '0135696'), 'LM_Calle_Antigua');


-- Regiones del Perú
INSERT INTO region (nombre) VALUES
                                ('AMAZONAS'),
                                ('ANCASH'),
                                ('APURIMAC'),
                                ('AREQUIPA'),
                                ('AYACUCHO'),
                                ('CAJAMARCA'),
                                ('CALLAO'),
                                ('CUSCO'),
                                ('HUANCAVELICA'),
                                ('HUANUCO'),
                                ('ICA'),
                                ('JUNIN'),
                                ('LA LIBERTAD'),
                                ('LAMBAYEQUE'),
                                ('LIMA'),
                                ('LORETO'),
                                ('MADRE DE DIOS'),
                                ('MOQUEGUA'),
                                ('PASCO'),
                                ('PIURA'),
                                ('PUNO'),
                                ('SAN MARTIN'),
                                ('TACNA'),
                                ('TUMBES'),
                                ('UCAYALI');

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
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ENERGIA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'ANALISTA DE ENERGIA')),

                                                                                                                 ('LEEANN ALEJANDRA', 'BENITES PALOMINO', '75001730', '924919500', 'l.benites@sudcomgroup.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'CW'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'SUPERVISORA DE OBRAS CIVILES')),

                                                                                                                 ('CROSBY', 'BRICEÑO MARAVI', '41589951', '993903920', 'cbriceno@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'ENTEL PERU S.A'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'COMERCIAL'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'GERENTE DE CUENTA COMERCIAL')),

                                                                                                                 ('ERICK JESUS', 'CABEZAS VILLAR', '70616017', '938989805', NULL,
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'PEXT'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'ANALISTA PEXT')),

                                                                                                                 ('VICTOR HUMBERTO', 'CHAVEZ JUAREZ', '16804166', NULL, NULL,
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ADMINISTRATIVA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'AUXILIAR DE OFICINA - DISCAPACIDAD')),

                                                                                                                 ('JESSICA IVETTE', 'CHIPANA DE LA CRUZ', '46468388', '927020297', 'proyectos3@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'ENTEL PERU S.A'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'CW'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'COORDINADOR DE INGENIERIA')),

                                                                                                                 ('KELLY TATIANA', 'CLEMENTE MARTINEZ', '48010945', '965239660', 'kclementem@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'SAQ'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'JEFE LEGAL SAQ')),

                                                                                                                 ('PEDRO RUDY', 'COLQUE ZATAN', '41026425', '969803234', 'pcolque@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'STL'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'PEXT'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'JEFE PEXT PINT')),

                                                                                                                 ('ERICK GABRIEL', 'CONTRERAS VALLE', '71539602', '969803234', 'econtreras@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'ENTEL PERU S.A'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ENTEL'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'GESTOR DE ACCESOS')),

                                                                                                                 ('NELSON GIOVANNY', 'COSSIO TRUJILLO', '77801770', '967816480', 'c.trujillo@sudcomgroup.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ENERGIA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'COORDINADOR DE ENERGIA')),

                                                                                                                 ('ENNY BELISSA BELEN', 'DE LA CRUZ MAYURI', '42226920', '974075107', 'prevencion@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'SSOMA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'SUPERVISORA DE SSOMA')),

                                                                                                                 ('GUILLERMO CARLOS', 'DIEGUEZ ALZAMORA', '3897788', '986031053', 'gdieguez@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'TI'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'COORDINADOR TI')),

                                                                                                                 ('ALVARO RODRIGO', 'FLORES SAAVEDRA', '42201982', '984177679', 'aflores@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'COMERCIAL'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'GERENTE COMERCIAL')),

                                                                                                                 ('MAGALLY DEL MILAGRO', 'GALINDO LEZAMA', '72315244', '978753064', 'mgalindo@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'CIERRE'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'JEFE DE CIERRE Y LIQUIDACIONES')),

                                                                                                                 ('JOSE CARLOS', 'GONZALEZ MUEDAS', '44373982', '991076898', 'jgonzales@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
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
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'CW'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'COORDINADOR DE CW')),

                                                                                                                 ('SILVIA ROSARIO', 'GUTIERREZ BURGOS', '25728504', '982521285', 's.gutierrez@sudcomgroup.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'COMERCIAL'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'ASISTENTE DE CONTRATACIONES PUBLICAS')),

                                                                                                                 ('HEIDY LISETH', 'HUAMAN CAVIEDES', '44388036', '901837213', 'hhuaman@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'SAQ'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'COORDINADOR LEGAL SAQ')),

                                                                                                                 ('LUIS ENRIQUE', 'LOAYZA LLOCCLLA', '73490407', '9500750970', 'l.loayza@sudcomgroup.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'ENTEL PERU S.A'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ENERGIA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'SUPERVISOR DE ENERGIA')),

                                                                                                                 ('RONALD PABLO', 'LOZANO SIERRA', '47580288', '934591631', 'proyectos1@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'ENTEL PERU S.A'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ENTEL'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'CADISTA')),

                                                                                                                 ('LUIS ANGEL', 'MARTINEZ ESTRADA', '72372882', '949963582', 'pext@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'PEXT'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'COORDINADOR PEXT')),

                                                                                                                 ('JESUS ALONSO', 'MARTINEZ YANQUI', '74919020', '972116383', 'jmartinez@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'TI'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'SUPERVISOR TI')),

                                                                                                                 ('GUILLERMO EDUARDO', 'MASIAS GUERRERO', '40999064', '922958952', 'sistemas@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'LOGÍSTICA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'ASISTENTE LOGISTICO')),

                                                                                                                 ('OLIVER ANTONIO', 'MASIAS LAGOS', '40264069', '993585214', 'omasias@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'GERENCIA GENERAL'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'GERENTE')),

                                                                                                                 ('ISAAC ROMULO', 'MELENDREZ FERNANDEZ', '6912965', '989181267', 'imelendez@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
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
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'ENTEL PERU S.A'),
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
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'CW-ENERGIA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'JEFE CW - JEFE DE ENERGIA')),

                                                                                                                 ('JORGE MIGUEL', 'OSORIO GALVEZ', '75419660', '994899418', 'josorio@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'TI'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'SUPERVISOR TI')),

                                                                                                                 ('JOSUE MANUEL RAUL', 'OTERO LOJE', '10731488', '987515878', 'jotero@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'LOGÍSTICA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'JEFE DE LOGÍSTICA')),

                                                                                                                 ('SEBASTIAN FELIX', 'OYOLA LOZA', '77812815', '923619930', 'soyola@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'ENTEL PERU S.A'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ENTEL'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'ASISTENTE LOGISTICO ENTEL')),

                                                                                                                 ('JOEL DANIEL', 'PAJUELO LUCIANDO', '78016138', '993440667', NULL,
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ENERGIA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'PRACTICANTE DE ENERGIA')),

                                                                                                                 ('JHAN CARLOS', 'POMACANCHARI QUISPE', '77177882', '983635967', NULL,
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'LOGÍSTICA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'AUXILIAR DE ALMACEN')),

                                                                                                                 ('AIDA LILIANA', 'REYNA CANDELA', '46196532', '997505944', 'areyna@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'CIERRE'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'JEFE DE CIERRE')),

                                                                                                                 ('HENRY JUNIOR', 'RODRIGUEZ YSLACHIN', '75404628', '916629404', 'henry.roys0500@gmail.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'CW'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'SUPERVISOR CW')),

                                                                                                                 ('TOÑO', 'ROMAN QUISPE', '41710568', '910871558', 't.roman@sudcomgroup.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ENERGIA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'COORDINADOR DE ENERGIA')),

                                                                                                                 ('MIGUEL ANGEL', 'SALAS CAMPOS', '32978147', '983276485', 'msalas@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'CW'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'SUPERVISOR CW')),

                                                                                                                 ('BRYAN ANTHONY', 'SALAZAR QUIJAITE', '45820159', '949875742', 'b.salazar@sudcomgroup.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'ENERGIA'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'SUPERVISOR DE ENERGIA')),

                                                                                                                 ('JHON DENNIS', 'SANCHEZ ALTAMIRANO', '17635270', '961715063', 'jsanchez@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'TI'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'PROJECT MANAGER')),

                                                                                                                 ('MARY CARMEN', 'SILVA GONZALES', '46580733', '915153318', 'legal3@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
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
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'PEXT'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'COORDINADOR PLANTA INTERNA')),

                                                                                                                 ('ANGELA LISBET', 'ZAMORA FLORES', '76256460', '993635707', 'a.zamora@sudcomgroup.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'CW'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'SUPERVISORA DE OBRAS CIVILES')),

                                                                                                                 ('MARIELA', 'NIEVA ARELLANO', '71244833', '958330870', 'costos@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'COMFUTURA'),
                                                                                                                  (SELECT id_area FROM area WHERE nombre = 'CONTABILIDAD'),
                                                                                                                  (SELECT id_cargo FROM cargo WHERE nombre = 'ASISTENTE DE CONTABILIDAD')),

                                                                                                                 ('JESUS OSWALDO', 'BARZOLA MALLMA', '41908627', '913591615', 'jbarzola@comfutura.com',
                                                                                                                  (SELECT id_empresa FROM empresa WHERE nombre = 'AMERICA MOVIL PERU S.A.C.'),
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


-- =====================================================
-- INSERCIONES DE PERMISOS DEL SISTEMA
-- =====================================================

INSERT INTO permiso (codigo, nombre, descripcion) VALUES
-- Permisos de Dashboard
('DASHBOARD_VIEW', 'Ver Dashboard', 'Permite ver el dashboard principal'),

-- Permisos de OT
('OT_VIEW', 'Ver OTs', 'Permite ver las Ordenes de Trabajo'),
('OT_CREATE', 'Crear OTs', 'Permite crear nuevas Ordenes de Trabajo'),
('OT_EDIT', 'Editar OTs', 'Permite editar Ordenes de Trabajo existentes'),

-- Permisos de Site
('SITE_VIEW', 'Ver Sites', 'Permite ver los Sites/Sitios'),
('SITE_MANAGE', 'Gestionar Sites', 'Permite crear y editar Sites'),

-- Permisos de Orden de Compra
('OC_VIEW', 'Ver OCs', 'Permite ver las Ordenes de Compra'),
('OC_CREATE', 'Crear OCs', 'Permite crear nuevas Ordenes de Compra'),
('OC_EDIT', 'Editar OCs', 'Permite editar Ordenes de Compra existentes'),

-- Permisos de Usuarios
('USUARIO_VIEW', 'Ver Usuarios', 'Permite ver la lista de usuarios'),
('USUARIO_CREATE', 'Crear Usuarios', 'Permite crear nuevos usuarios'),
('USUARIO_EDIT', 'Editar Usuarios', 'Permite editar usuarios existentes'),
('USUARIO_DELETE', 'Eliminar Usuarios', 'Permite eliminar usuarios'),
('USUARIO_ADMIN', 'Administrar Usuarios', 'Permiso completo de administración de usuarios'),

-- Permisos de Gestión de Permisos
('PERMISO_ADMIN', 'Administrar Permisos', 'Permite gestionar permisos del sistema'),

-- Permisos de Trabajadores
('TRABAJADOR_VIEW', 'Ver Trabajadores', 'Permite ver la lista de trabajadores'),
('TRABAJADOR_MANAGE', 'Gestionar Trabajadores', 'Permite crear y editar trabajadores'),

-- Permisos de Clientes
('CLIENTE_VIEW', 'Ver Clientes', 'Permite ver la lista de clientes'),
('CLIENTE_MANAGE', 'Gestionar Clientes', 'Permite crear y editar clientes'),

-- Permisos de Configuración
('CONFIGURACION_VIEW', 'Ver Configuración', 'Permite ver configuraciones del sistema'),
('CONFIGURACION_MANAGE', 'Gestionar Configuración', 'Permite modificar configuraciones'),

-- Permisos de Reportes
('REPORTES_VIEW', 'Ver Reportes', 'Permite ver reportes del sistema'),
('REPORTES_GENERAR', 'Generar Reportes', 'Permite generar nuevos reportes'),

-- Permisos de Perfil
('PERFIL_VIEW', 'Ver Perfil', 'Permite ver el perfil de usuario'),
('PERFIL_EDIT', 'Editar Perfil', 'Permite editar el perfil de usuario');

-- =====================================================
-- ASIGNACIÓN POR NIVELES (L1 a L5)
-- =====================================================

-- L1 - Gerencia General (TODOS los permisos de administración)
INSERT INTO permiso_nivel (id_permiso, id_nivel)
SELECT p.id_permiso, n.id_nivel
FROM permiso p, nivel n
WHERE n.codigo = 'L1'  -- Gerencia General
  AND p.codigo IN (
                   'DASHBOARD_VIEW',
                   'OT_VIEW', 'OT_CREATE', 'OT_EDIT',
                   'SITE_VIEW', 'SITE_MANAGE',
                   'OC_VIEW', 'OC_CREATE', 'OC_EDIT',
                   'USUARIO_VIEW', 'USUARIO_CREATE', 'USUARIO_EDIT', 'USUARIO_DELETE',
                   'PERMISO_ADMIN',
                   'TRABAJADOR_VIEW', 'TRABAJADOR_MANAGE',
                   'CLIENTE_VIEW', 'CLIENTE_MANAGE',
                   'CONFIGURACION_VIEW', 'CONFIGURACION_MANAGE',
                   'REPORTES_VIEW', 'REPORTES_GENERAR',
                   'PERFIL_VIEW', 'PERFIL_EDIT'
    );

-- L2 - Gerencia/Subgerencia (casi todos, excepto eliminación de usuarios)
INSERT INTO permiso_nivel (id_permiso, id_nivel)
SELECT p.id_permiso, n.id_nivel
FROM permiso p, nivel n
WHERE n.codigo = 'L2'  -- Gerencia/Subgerencia
  AND p.codigo IN (
                   'DASHBOARD_VIEW',
                   'OT_VIEW', 'OT_CREATE', 'OT_EDIT',
                   'SITE_VIEW', 'SITE_MANAGE',
                   'OC_VIEW', 'OC_CREATE', 'OC_EDIT',
                   'USUARIO_VIEW', 'USUARIO_CREATE', 'USUARIO_EDIT',
                   'TRABAJADOR_VIEW', 'TRABAJADOR_MANAGE',
                   'CLIENTE_VIEW', 'CLIENTE_MANAGE',
                   'CONFIGURACION_VIEW',
                   'REPORTES_VIEW', 'REPORTES_GENERAR',
                   'PERFIL_VIEW', 'PERFIL_EDIT'
    );

-- L3 - Jefatura (permisos operativos y de supervisión)
INSERT INTO permiso_nivel (id_permiso, id_nivel)
SELECT p.id_permiso, n.id_nivel
FROM permiso p, nivel n
WHERE n.codigo = 'L3'  -- Jefatura
  AND p.codigo IN (
                   'DASHBOARD_VIEW',
                   'OT_VIEW', 'OT_CREATE', 'OT_EDIT',
                   'SITE_VIEW', 'SITE_MANAGE',
                   'OC_VIEW', 'OC_CREATE',
                   'TRABAJADOR_VIEW',
                   'CLIENTE_VIEW',
                   'REPORTES_VIEW',
                   'PERFIL_VIEW', 'PERFIL_EDIT'
    );

-- L4 - Coordinación (permisos operativos básicos)
INSERT INTO permiso_nivel (id_permiso, id_nivel)
SELECT p.id_permiso, n.id_nivel
FROM permiso p, nivel n
WHERE n.codigo = 'L4'  -- Coordinación
  AND p.codigo IN (
                   'DASHBOARD_VIEW',
                   'OT_VIEW', 'OT_CREATE',
                   'SITE_VIEW',
                   'OC_VIEW',
                   'PERFIL_VIEW', 'PERFIL_EDIT'
    );

-- L5 - Operativo (mínimos permisos)
INSERT INTO permiso_nivel (id_permiso, id_nivel)
SELECT p.id_permiso, n.id_nivel
FROM permiso p, nivel n
WHERE n.codigo = 'L5'  -- Operativo
  AND p.codigo IN (
                   'DASHBOARD_VIEW',
                   'OT_VIEW',
                   'PERFIL_VIEW'
    );-- =====================================================
-- ASIGNACIÓN POR ÁREAS (específicas por departamento)
-- =====================================================

-- ADMIN / SISTEMAS / TI - Permisos de configuración
INSERT INTO permiso_area (id_permiso, id_area)
SELECT p.id_permiso, a.id_area
FROM permiso p, area a
WHERE a.nombre IN ('ADMIN', 'SISTEMAS', 'TI')
  AND p.codigo IN (
                   'CONFIGURACION_VIEW', 'CONFIGURACION_MANAGE',
                   'PERMISO_ADMIN',
                   'USUARIO_VIEW', 'USUARIO_CREATE', 'USUARIO_EDIT'
    );

-- RRHH - Permisos de trabajadores
INSERT INTO permiso_area (id_permiso, id_area)
SELECT p.id_permiso, a.id_area
FROM permiso p, area a
WHERE a.nombre = 'RRHH'
  AND p.codigo IN (
                   'TRABAJADOR_VIEW', 'TRABAJADOR_MANAGE',
                   'USUARIO_VIEW', 'USUARIO_CREATE', 'USUARIO_EDIT'
    );

-- COSTOS / FINANZAS / CONTABILIDAD - Permisos financieros
INSERT INTO permiso_area (id_permiso, id_area)
SELECT p.id_permiso, a.id_area
FROM permiso p, area a
WHERE a.nombre IN ('COSTOS', 'FINANZAS', 'CONTABILIDAD')
  AND p.codigo IN (
                   'OC_VIEW', 'OC_CREATE', 'OC_EDIT',
                   'REPORTES_VIEW', 'REPORTES_GENERAR'
    );

-- COMERCIAL - Permisos de clientes
INSERT INTO permiso_area (id_permiso, id_area)
SELECT p.id_permiso, a.id_area
FROM permiso p, area a
WHERE a.nombre = 'COMERCIAL'
  AND p.codigo IN (
                   'CLIENTE_VIEW', 'CLIENTE_MANAGE',
                   'OT_VIEW', 'OT_CREATE'
    );

-- CW / ENERGIA / PEXT - Permisos técnicos
INSERT INTO permiso_area (id_permiso, id_area)
SELECT p.id_permiso, a.id_area
FROM permiso p, area a
WHERE a.nombre IN ('CW', 'ENERGIA', 'PEXT', 'CW-ENERGIA')
  AND p.codigo IN (
                   'OT_VIEW', 'OT_CREATE', 'OT_EDIT',
                   'SITE_VIEW', 'SITE_MANAGE'
    );

-- LOGÍSTICA - Permisos de logística
INSERT INTO permiso_area (id_permiso, id_area)
SELECT p.id_permiso, a.id_area
FROM permiso p, area a
WHERE a.nombre = 'LOGÍSTICA'
  AND p.codigo IN (
                   'SITE_VIEW',
                   'OC_VIEW', 'OC_CREATE'
    );

-- SSOMA - Permisos de seguridad
INSERT INTO permiso_area (id_permiso, id_area)
SELECT p.id_permiso, a.id_area
FROM permiso p, area a
WHERE a.nombre = 'SSOMA'
  AND p.codigo IN (
                   'SITE_VIEW', 'SITE_MANAGE',
                   'REPORTES_VIEW'
    );

-- SAQ / ENTEL - Permisos específicos de proyectos
INSERT INTO permiso_area (id_permiso, id_area)
SELECT p.id_permiso, a.id_area
FROM permiso p, area a
WHERE a.nombre IN ('SAQ', 'ENTEL')
  AND p.codigo IN (
                   'OT_VIEW', 'OT_CREATE',
                   'SITE_VIEW'
    );-- =====================================================
-- ASIGNACIÓN POR CARGOS ESPECÍFICOS
-- =====================================================

-- GERENTE - Todos los permisos de su área
INSERT INTO permiso_cargo (id_permiso, id_cargo)
SELECT p.id_permiso, c.id_cargo
FROM permiso p, cargo c
WHERE c.nombre = 'GERENTE'
  AND p.codigo IN (
                   'DASHBOARD_VIEW',
                   'OT_VIEW', 'OT_CREATE', 'OT_EDIT',
                   'SITE_VIEW', 'SITE_MANAGE',
                   'OC_VIEW', 'OC_CREATE', 'OC_EDIT',
                   'REPORTES_VIEW', 'REPORTES_GENERAR',
                   'PERFIL_VIEW', 'PERFIL_EDIT'
    );

-- JEFE TI - Permisos de sistemas
INSERT INTO permiso_cargo (id_permiso, id_cargo)
SELECT p.id_permiso, c.id_cargo
FROM permiso p, cargo c
WHERE c.nombre = 'JEFE TI'
  AND p.codigo IN (
                   'CONFIGURACION_VIEW', 'CONFIGURACION_MANAGE',
                   'PERMISO_ADMIN',
                   'USUARIO_VIEW', 'USUARIO_CREATE', 'USUARIO_EDIT'
    );

-- JEFA DE FINANZAS - Permisos financieros
INSERT INTO permiso_cargo (id_permiso, id_cargo)
SELECT p.id_permiso, c.id_cargo
FROM permiso p, cargo c
WHERE c.nombre = 'JEFA DE FINANZAS'
  AND p.codigo IN (
                   'OC_VIEW', 'OC_CREATE', 'OC_EDIT',
                   'REPORTES_VIEW', 'REPORTES_GENERAR'
    );

-- COORDINADOR TI - Permisos de configuración limitados
INSERT INTO permiso_cargo (id_permiso, id_cargo)
SELECT p.id_permiso, c.id_cargo
FROM permiso p, cargo c
WHERE c.nombre = 'COORDINADOR TI'
  AND p.codigo IN (
                   'CONFIGURACION_VIEW',
                   'USUARIO_VIEW', 'USUARIO_EDIT'
    );

-- JEFE DE LOGÍSTICA - Permisos logísticos
INSERT INTO permiso_cargo (id_permiso, id_cargo)
SELECT p.id_permiso, c.id_cargo
FROM permiso p, cargo c
WHERE c.nombre = 'JEFE DE LOGÍSTICA'
  AND p.codigo IN (
                   'SITE_VIEW', 'SITE_MANAGE',
                   'OC_VIEW', 'OC_CREATE'
    );

-- SUPERVISOR CW - Permisos de supervisión técnica
INSERT INTO permiso_cargo (id_permiso, id_cargo)
SELECT p.id_permiso, c.id_cargo
FROM permiso p, cargo c
WHERE c.nombre = 'SUPERVISOR CW'
  AND p.codigo IN (
                   'OT_VIEW', 'OT_CREATE', 'OT_EDIT',
                   'SITE_VIEW'
    );

-- ANALISTA FINANCIERO - Permisos de análisis
INSERT INTO permiso_cargo (id_permiso, id_cargo)
SELECT p.id_permiso, c.id_cargo
FROM permiso p, cargo c
WHERE c.nombre = 'ANALISTA FINANCIERO'
  AND p.codigo IN (
                   'OC_VIEW',
                   'REPORTES_VIEW'
    );

-- ASISTENTE DE RRHH - Permisos básicos de personal
INSERT INTO permiso_cargo (id_permiso, id_cargo)
SELECT p.id_permiso, c.id_cargo
FROM permiso p, cargo c
WHERE c.nombre = 'ASISTENTE DE RRHH'
  AND p.codigo IN (
                   'TRABAJADOR_VIEW',
                   'PERFIL_VIEW'
    );

-- JEFATURA RESPONSABLE - Permisos de aprobación
INSERT INTO permiso_cargo (id_permiso, id_cargo)
SELECT p.id_permiso, c.id_cargo
FROM permiso p, cargo c
WHERE c.nombre = 'JEFATURA RESPONSABLE'
  AND p.codigo IN (
                   'OT_VIEW', 'OT_EDIT',
                   'OC_VIEW', 'OC_EDIT'
    );

-- LIQUIDADOR - Permisos de liquidación
INSERT INTO permiso_cargo (id_permiso, id_cargo)
SELECT p.id_permiso, c.id_cargo
FROM permiso p, cargo c
WHERE c.nombre = 'LIQUIDADOR'
  AND p.codigo IN (
                   'OT_VIEW',
                   'OC_VIEW', 'OC_CREATE'
    );

-- EJECUTANTE - Permisos de ejecución
INSERT INTO permiso_cargo (id_permiso, id_cargo)
SELECT p.id_permiso, c.id_cargo
FROM permiso p, cargo c
WHERE c.nombre = 'EJECUTANTE'
  AND p.codigo IN (
                   'OT_VIEW',
                   'SITE_VIEW'
    );

-- ANALISTA CONTABLE - Permisos contables
INSERT INTO permiso_cargo (id_permiso, id_cargo)
SELECT p.id_permiso, c.id_cargo
FROM permiso p, cargo c
WHERE c.nombre = 'ANALISTA CONTABLE'
  AND p.codigo IN (
                   'OC_VIEW',
                   'REPORTES_VIEW'
    );-- =====================================================
-- INSERCIONES DE TRABAJADORES DE PRUEBA
-- =====================================================

INSERT INTO `maestro_codigo` VALUES (1,'S000001','GESTIÓN DE NUEVO SUMINISTRO DE 19.9KW + INSTALACIÓN DE BREAKER TRIFÁSICO + CABLEADO PARA SWAP DE RECTIFICADOR',1,4.20,1,'2026-01-22 14:51:37'),(2,'S000002','INSTALACION DE SOPORTES RF',1,4.20,1,'2026-01-22 14:51:37'),(3,'S000003','DISEÑO DE PLANTA EXTERNA',1,5.00,1,'2026-01-22 14:51:37'),(4,'S000004','SERVICIO DE OBRAS CIVILES PARA EL REFORZAMIENTO Y PINTADO DE ESTRUCTURAS METALICAS DE TORRE EXISTENTE',1,6.00,1,'2026-01-22 14:51:37'),(5,'S000005','SWAP RRU DUAL BAND 700-850MHz 2 SECTORES',1,5.00,1,'2026-01-22 14:51:37'),(6,'S000006','SWAP DE TARJETA',1,0.00,1,'2026-01-22 14:51:37'),(7,'S000007','VISITA DE RELEVAMIENTO',1,0.00,1,'2026-01-22 14:51:37'),(8,'S000008','IMPLEMENTACION DE 4G 2600MHz - COUBICACION 2600 B7 1 SECTOR',1,0.00,1,'2026-01-22 14:51:37'),(9,'S000009','IMPLEMENTACION DE 4G 2600MHz - TDD 2.6 B38 1 SECTOR',1,0.00,1,'2026-01-22 14:51:37'),(10,'S000010','IMPLEMENTACION DE 4G 2600MHz - TDD 2.6 B38 2 SECTORES',1,0.00,1,'2026-01-22 14:51:37'),(11,'S000011','IMPLEMENTACION DE 4G 2600MHz - TDD 2.6 B38 3 SECTORES + INSTALACION DE EQUIPOS DE ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(12,'S000012','ADECUACION DE TABLERO DE AGREGADOR PARA PASE DE CABLEADO DE ENERGÍA+INSTALACION DE TABLERO DE SOMERAS',1,0.00,1,'2026-01-22 14:51:37'),(13,'S000013','INGRESO Y SUBSANACION DE EXPEDIENTES DE LICENCIAS+PRESENTACION DE CARTA DE INICIO Y CULMINACION DE OBRA',1,0.00,1,'2026-01-22 14:51:37'),(14,'S000014','SUMINISTRO DE ESTRUCTURAS METALICAS P/REFORZAMIENTO DE MONTANTES SEGÚN PLANO',1,0.00,1,'2026-01-22 14:51:37'),(15,'S000015','TENDIDO DE FIBRA OPTICA INCLUYE MATERIALES',2,0.00,1,'2026-01-22 14:51:37'),(16,'S000016','IMPLEMENTACION DE 3G_4G 3 SECTORES 4 TECNOLOGIAS + INSTALACION DE EQUIPOS ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(17,'S000017','INSTALACION DE ENLACE MW',1,0.00,1,'2026-01-22 14:51:37'),(18,'S000018','IMPLEMENTACION DE 4G 700MHz 3 SECTORES',1,0.00,1,'2026-01-22 14:51:37'),(19,'S000019','IMPLEMENTACION DE 4G 700MHz 1 SECTOR',1,0.00,1,'2026-01-22 14:51:37'),(20,'S000020','IMPLEMENTACION DE 1 SECTOR ADICIONAL 4 TECNOLOGIAS',1,0.00,1,'2026-01-22 14:51:37'),(21,'S000021','INSTALACION DE POWER CORE',1,0.00,1,'2026-01-22 14:51:37'),(22,'S000022','SWAP RRU DUAL BAND 700-850MHz 3 SECTORES',1,0.00,1,'2026-01-22 14:51:37'),(23,'S000023','INSTALACION DE RRU',1,0.00,1,'2026-01-22 14:51:37'),(24,'S000024','INSTALACION DE RRU 2600MHz + SWAP DE ANTENA + SWAP DE POWER CORE',1,0.00,1,'2026-01-22 14:51:37'),(25,'S000025','IMPLEMENTACION DE 4G 2600MHz 1 SECTOR + INSTALACION DE EQUIPOS ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(26,'S000026','IMPLEMENTACION DE 3G 850MHZ, 4G 700_1900MHZ 1 SECTOR + INSTALACION DE EQUIPOS ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(27,'S000027','IMPLEMENTACION DE 4G 2600MHz 3 SECTORES + INSTALACION DE EQUIPOS ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(28,'S000028','INSTALACIÓN DE POSTE METÁLICO + 1 POZO SISTEMA PARRES + TABLERO EN CAJA METÁLICA + SOPORTES RF',1,0.00,1,'2026-01-22 14:51:37'),(29,'S000029','INSTALACION DE CELL ROUTER',1,0.00,1,'2026-01-22 14:51:37'),(30,'S000030','OBRAS CIVILES',1,0.00,1,'2026-01-22 14:51:37'),(31,'S000031','INSTALACION ELECTRICA Y SPAT',1,0.00,1,'2026-01-22 14:51:37'),(32,'S000032','INSTALACION DE TORRE AUTOSOPORTADA TRIANGULAR 15 MTS',1,0.00,1,'2026-01-22 14:51:37'),(33,'S000033','SUMINISTRO DE TABLERO + ACCESORIOS DE TORRE',1,0.00,1,'2026-01-22 14:51:37'),(34,'S000034','HABILITACION DE HILOS DE FO PARA TRANSMISION',1,0.00,1,'2026-01-22 14:51:37'),(35,'S000035','SUMINISTRO DE TORRE AUTOSOPORTADA TRIANGULAR 15 MTS',1,0.00,1,'2026-01-22 14:51:37'),(36,'S000036','SUMINISTRO DE SOPORTE RF 3MTS REGULABLE',1,0.00,1,'2026-01-22 14:51:37'),(37,'S000037','MEDICION RNI',1,0.00,1,'2026-01-22 14:51:37'),(38,'S000038','ELABORACION EXPEDIENTE E IMPLEMENTACION CIRA',1,0.00,1,'2026-01-22 14:51:37'),(39,'S000039','ELABORACION EXPEDIENTE E IMPLEMENTACION PMA',1,0.00,1,'2026-01-22 14:51:37'),(40,'S000040','IMPLEMENTACION DE 4G 700MHz 2 SECTORES',1,0.00,1,'2026-01-22 14:51:37'),(41,'S000041','INSTALACION DE TORRE AUTOSOPORTADA CUADRARA 15 MTS',1,0.00,1,'2026-01-22 14:51:37'),(42,'S000042','IMPLEMENTACION DE 3G_4G 4 SECTORES 4 TECNOLOGIAS + INSTALACION DE EQUIPOS ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(43,'S000043','IMPLEMENTACION DE 3G_4G 1 SECTOR 4 TECNOLOGIAS + INSTALACION DE EQUIPOS ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(44,'S000044','ADICIONAL POR ADECUACIONES, CAMBIO DE BREAKER Y TRABAJO DE RESANES',1,0.00,1,'2026-01-22 14:51:37'),(45,'S000045','IMPLEMENTACION DE 3G 850MHZ, 4G 700_2600MHZ 2 SECTORES',1,0.00,1,'2026-01-22 14:51:37'),(46,'S000046','TSS DE POSTES NO AUTORIZADOS',1,0.00,1,'2026-01-22 14:51:37'),(47,'S000047','SERVICIO DE TRANSPORTE DE CARGA TERRESTRE',1,0.00,1,'2026-01-22 14:51:37'),(48,'S000048','INSTALACION DE POSTE Y SOPORTES + OBRA CIVIL',1,0.00,1,'2026-01-22 14:51:37'),(49,'S000049','DESMONTAJE DE POSTE DE 15 MTS Y SOPORTES',1,0.00,1,'2026-01-22 14:51:37'),(50,'S000050','IMPLEMENTACION DE 3G_4G 4 SECTORES 2 TECNOLOGIAS + INSTALACION DE EQUIPOS ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(51,'S000051','MEDICION DE PARAMETROS ELECTRICOS',1,0.00,1,'2026-01-22 14:51:37'),(52,'S000052','INFORME PSICOLOGICO',1,0.00,1,'2026-01-22 14:51:37'),(53,'S000053','INFORME CREDITICIO',1,0.00,1,'2026-01-22 14:51:37'),(54,'S000054','ESTUDIO DE MECANICA DE SUELOS',1,0.00,1,'2026-01-22 14:51:37'),(55,'S000055','ESTUDIO DE RESISTIVIDAD',1,0.00,1,'2026-01-22 14:51:37'),(56,'S000056','DISEÑO DE CIMENTACION',1,0.00,1,'2026-01-22 14:51:37'),(57,'S000057','SUMINISTRO E INSTALACION DE POSTE 9 MTS',1,0.00,1,'2026-01-22 14:51:37'),(58,'S000058','SUMINISTRO E INSTALACION DE POSTE 11 MTS',1,0.00,1,'2026-01-22 14:51:37'),(59,'S000059','REUBICACION DE POSTE CAC + OBRA CIVIL',1,0.00,1,'2026-01-22 14:51:37'),(60,'S000060','IMPLEMENTACION DE 3G 850MHZ, 4G 700_1900_2600MHZ 1 SECTOR',1,0.00,1,'2026-01-22 14:51:37'),(61,'S000061','IMPLEMENTACION DE 3G 850MHZ, 4G 700_1900MHZ 3 SECTORES + INSTALACION DE EQUIPOS ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(62,'S000062','REUBICACION DE MASSIVE MIMO 1 SECTOR + INSTALACION DE SOPORTE RF',1,0.00,1,'2026-01-22 14:51:37'),(63,'S000063','ELABORACION DE EXPEDIENTE TECNICO PARA LA DGAC',1,0.00,1,'2026-01-22 14:51:37'),(64,'S000064','APERTURA DE HOYO, IZADO Y RELLENO POSTE 9/300',2,0.00,1,'2026-01-22 14:51:37'),(65,'S000065','APERTURA DE HOYO, IZADO Y RELLENO POSTE 11/300',2,0.00,1,'2026-01-22 14:51:37'),(66,'S000066','IMPLEMENTACION DE 4G 2600MHz - TDD 2.6 B38, 1900MHz 1 SECTOR',1,0.00,1,'2026-01-22 14:51:37'),(67,'S000067','IMPLEMENTACION DE MASSIVE MIMO 1 SECTOR + INSTALACION DE EQUIPOS DE ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(68,'S000068','IMPLEMENTACION DE MASSIVE MIMO 2 SECTORES + INSTALACION DE EQUIPOS DE ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(69,'S000069','IMPLEMENTACION DE 4G 2600MHz - TDD 2.6 B38, SWAP RRU 1900MHz 1 SECTOR + COUBICACION 2600 B7 2 SECTORES + INSTALACION DE EQUIPOS ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(70,'S000070','APERTURA DE HOYO, IZADO Y RELLENO POSTE',1,0.00,1,'2026-01-22 14:51:37'),(71,'S000071','INSTALACION DE SPAT',1,0.00,1,'2026-01-22 14:51:37'),(72,'S000072','VISITA PARA TSS',1,0.00,1,'2026-01-22 14:51:37'),(73,'S000073','ACOMETIDA ELECTRICA',1,0.00,1,'2026-01-22 14:51:37'),(74,'S000074','TRABAJO DE CHAFLANES',1,0.00,1,'2026-01-22 14:51:37'),(75,'S000075','TECHO SHELTER 5X2.5 IMPERMEABILIZADO',1,0.00,1,'2026-01-22 14:51:37'),(76,'S000076','AMPLIACION TECHO 2X3 M ESTRUCTURADO CON TUBOS GALVANIZADOS',1,0.00,1,'2026-01-22 14:51:37'),(77,'S000077','PARAPETO EN AREA DE UE ESTRUCTURADO CON TUBOS GALVANIZADOS Y RECUBIERTO CON PLANCHA ADUCIN',1,0.00,1,'2026-01-22 14:51:37'),(78,'S000078','CAMBIO DE TECHO TOTAL CON ADUCIN',1,0.00,1,'2026-01-22 14:51:37'),(79,'S000079','SELLADO CON SILICONA DE LUMINARIAS Y TOMACORRIENTES',1,0.00,1,'2026-01-22 14:51:37'),(80,'S000080','SPAT SITE MODEL',1,0.00,1,'2026-01-22 14:51:37'),(81,'S000081','ATERRAMIENTO DE ESTRUCTURAS METÁLICAS Y PARARRAYOS',1,0.00,1,'2026-01-22 14:51:37'),(82,'S000082','VERTICALIDAD DE TORRE',1,0.00,1,'2026-01-22 14:51:37'),(83,'S000083','INSTALACION DE ELEMENTOS FALTANTES EN TORRE Y COLOCACION DE GROUTING',1,0.00,1,'2026-01-22 14:51:37'),(84,'S000084','DISEÑO DE MEZCLA Y ROTURA DE 4 PROBETAS',1,0.00,1,'2026-01-22 14:51:37'),(85,'S000085','INSTALACION DE PIEDRA CHANCADA Y LIMPIEZA FINAL DEL SITE',1,0.00,1,'2026-01-22 14:51:37'),(86,'S000086','MALLA PARA SPAT',1,0.00,1,'2026-01-22 14:51:37'),(87,'S000087','MANTENIMIENTO DE TORRE AUTOSOPORTADA',1,0.00,1,'2026-01-22 14:51:37'),(88,'S000088','MANTENIMIENTO PREVENTIVO E IMPERMEABILIZACION',1,0.00,1,'2026-01-22 14:51:37'),(89,'S000089','SWAP DE ANTENA',1,0.00,1,'2026-01-22 14:51:37'),(90,'S000090','SWAP RRU 2600MHz',1,0.00,1,'2026-01-22 14:51:37'),(91,'S000091','SANEAMIENTO DE SITIO',1,0.00,1,'2026-01-22 14:51:37'),(92,'S000092','ALQUILER DE CAMIONETA',1,0.00,1,'2026-01-22 14:51:37'),(93,'S000093','MANO DE OBRA PARA REPOSICION DE SPAT',1,0.00,1,'2026-01-22 14:51:37'),(94,'S000094','TENDIDO DE FIBRA OPTICA',2,0.00,1,'2026-01-22 14:51:37'),(95,'S000095','SUMINISTRO DE DOS POZOS A TIERRA INCLUÍDO MALLA DE 35 METROS CON 18 PUNTOS DE SOLDADURA EXOTERMICA',1,0.00,1,'2026-01-22 14:51:37'),(96,'S000096','IMPLEMENTACION DE 4G 1900_2600MHZ 1 SECTOR',1,0.00,1,'2026-01-22 14:51:37'),(97,'S000097','IMPLEMENTACION DE 3G 850MHZ, 4G 700_2600MHZ 1 SECTOR',1,0.00,1,'2026-01-22 14:51:37'),(98,'S000098','DESMONTAJE TOTAL DEL SITE',1,0.00,1,'2026-01-22 14:51:37'),(99,'S000099','MANTENIMIENTO Y SANEAMIENTO DEL SITE',1,0.00,1,'2026-01-22 14:51:37'),(100,'S000100','ABRAZADERAS Y PERNERIAS',1,0.00,1,'2026-01-22 14:51:37'),(101,'S001568','EXAMEN MEDICO OCUPACIONAL',1,0.00,1,'2026-01-22 14:51:37'),(102,'S001567','SUMINISTRO E INSTALACION PARA ANCLAJE DE 8 POSTES',1,0.00,1,'2026-01-22 14:51:37'),(103,'S001569','ALMACENAJE',1,0.00,1,'2026-01-22 14:51:37'),(104,'S002328','ADICIONAL AMPLIACION DE POTENCIA',1,0.00,1,'2026-01-22 14:51:37'),(105,'S002329','TRABAJOS DE CULMINACION DE OBRA EN SITE',1,0.00,1,'2026-01-22 14:51:37'),(106,'S002330','CONSTRUCCION DE MURETE Y CONEXIÓN DE ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(107,'S002331','SOPORTE REFORZADO TIPO PLATAFORMA PARA GABINETE MINI PACK',1,0.00,1,'2026-01-22 14:51:37'),(108,'S002332','DEMOLICION DE CIMENTACION DE MONOPOLO Y MURO DE LADRILLO, REPOSICION DE MURO DE LADRILLO Y RELLENO PARA NIVELACION',1,0.00,1,'2026-01-22 14:51:37'),(109,'S002333','IMPLEMENTACION DE 4G 2600MHz 2 SECTORES + INSTALACION DE EQUIPOS ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(110,'S002334','DESMONTAJE DE TORRE AUTOSOPORTADA',1,0.00,1,'2026-01-22 14:51:37'),(111,'S002335','DEMOLICION DE BASES DE TORRE Y CERCO PERIMETRICO',1,0.00,1,'2026-01-22 14:51:37'),(112,'S002336','ELIMINACION DE DESECHOS DE CONCRETO CON PERMISO MUNICIPAL',1,0.00,1,'2026-01-22 14:51:37'),(113,'S002337','TRASLADO DE POSTE DE PIURA A TALARA',1,0.00,1,'2026-01-22 14:51:37'),(114,'S002338','EXCAVACION DE ZANJA PARA ZAPATA',1,0.00,1,'2026-01-22 14:51:37'),(115,'S002339','ELIMINACION DE DESMONTE',1,0.00,1,'2026-01-22 14:51:37'),(116,'S002340','SOLADO DE CONCRETO DE 10CM DE ESPESOR',1,0.00,1,'2026-01-22 14:51:37'),(117,'S002341','CONCRETO PARA CIMENTACION DE POSTE F´C=175KG/CM2',1,0.00,1,'2026-01-22 14:51:37'),(118,'S002342','PIEDRA BASE PARA CIMENTACION',1,0.00,1,'2026-01-22 14:51:37'),(119,'S002343','GRUA PARA DESMONTAJE DE TRAYLER E IZAJE DE POSTE DE 15/1200 MTS y SOSTENER HASTA QUE FRAGUE EL CONCRETO',1,0.00,1,'2026-01-22 14:51:37'),(120,'S002344','CONSTRUCCION DE POZO A TIERRA TIPO PARRES DE 1.20M DE PROFUNDIDAD Y COLOCACION DE BARRA DE ATERRAMIENTOS',1,0.00,1,'2026-01-22 14:51:37'),(121,'S002345','COLOCACION DE CARPINTERIA METALICA EN POSTE (SOPORTES PARA ANTENAS, GABINETES ESCALERILLAS)',1,0.00,1,'2026-01-22 14:51:37'),(122,'S002346','COLOCACION DE TABLERO ELECTRICOS Y CONEXIONES A MURETE DE MEDIDOR',1,0.00,1,'2026-01-22 14:51:37'),(123,'S002347','GRUA PARA DESMONTAR POSTE EXISTENTE EN MAL ESTADO',1,0.00,1,'2026-01-22 14:51:37'),(124,'S002348','EXCAVACION DE ZANJA PARA RETIRO DE POSTE EXISTENTE EN MAL ESTADO, ELIMINACION Y DESMOTAJE DE POSTE HASTA EL RELLENO SANITARIO AFUERAS DE LA CIUDAD',1,0.00,1,'2026-01-22 14:51:37'),(125,'S002349','DELIMITACION DE TERRENOS, MOVILIZACION HERRAMIENTAS Y EQUIPOS',1,0.00,1,'2026-01-22 14:51:37'),(126,'S002350','IMPLEMENTACION DE 1 SECTOR ADICIONAL ANTENA ADU',1,0.00,1,'2026-01-22 14:51:37'),(127,'S002351','DESMONTAJE DE EQUIPOS RF Y ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(128,'S002352','ADICIONAL POR AMPLIACION DE POTENCIA - LINEA DE BAJA TENSION 750 MTS',1,0.00,1,'2026-01-22 14:51:37'),(129,'S002353','ADICIONAL POR AMPLIACION DE POTENCIA - LINEA DE BAJA TENSION 150 MTS',1,0.00,1,'2026-01-22 14:51:37'),(130,'S002354','LEVANTAMIENTO DE OBSERVACIONES POR MANTENIMIENTO PROVENTIVO, REPOSICION DE SPAT Y LIMPIEZA',1,0.00,1,'2026-01-22 14:51:37'),(131,'S002355','LIQUIDACION POR EXAMENES MEDICOS, SCTR POR SERVICIO FALLIDO DE MANTENIMIENTO FEN 2023',1,0.00,1,'2026-01-22 14:51:37'),(132,'S002356','DESMONTAJE DE TORRE VENTADA',1,0.00,1,'2026-01-22 14:51:37'),(133,'S002357','DESMONTAJE DE CERCO METALICO',1,0.00,1,'2026-01-22 14:51:37'),(134,'S002358','ENERGIA PROVISIONAL Y TENDIDO ELECTRICO',1,0.00,1,'2026-01-22 14:51:37'),(135,'S002359','REUBICACION DE EQUIPOS RF Y ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(136,'S002360','INSTALACION DE POSTE CAC 9 MTS',1,0.00,1,'2026-01-22 14:51:37'),(137,'S002361','VALIDACION EN CAMPO DE HILOS, FUSIONES EN FAR END E INSTALACION DE CAJA PANDUIT',1,0.00,1,'2026-01-22 14:51:37'),(138,'S002362','RELEVAMIENTO DE TORRE Y EVALUACION ESTRUCTURAL',1,0.00,1,'2026-01-22 14:51:37'),(139,'S002363','ALQUILER DE GRUA',3,0.00,1,'2026-01-22 14:51:37'),(140,'S002364','EXCAVACION Y ELIMINACION DE CIMENTACION',1,0.00,1,'2026-01-22 14:51:37'),(141,'S002365','INSTALACIÓN DE SOPORTES RF + TABLERO',1,0.00,1,'2026-01-22 14:51:37'),(142,'S002366','BUSQUEDA DE CANDIDATOS CON VALIDACION DE TSSV, SARV  Y KMZ',1,0.00,1,'2026-01-22 14:51:37'),(143,'S002367','EMPALME ADICIONAL, INCLUYE FUSION Y MEDICIONES',1,0.00,1,'2026-01-22 14:51:37'),(144,'S002368','DESMONTAJE E INSTALACION DE EQUIPOS RF Y ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(145,'S002369','DESMONTAJE DE VIGAS H',1,0.00,1,'2026-01-22 14:51:37'),(146,'S002370','DEMOLICION DE 3 CAMARAS DE ANCLAJE, BASES DE TORRE, PEDESTALES DE LAS VIGAS H Y RETIRO DE DESMONTE',1,0.00,1,'2026-01-22 14:51:37'),(147,'S002371','IMPLEMENTACION DE CABLEADO ESTRUCTURADO WIFI',1,0.00,1,'2026-01-22 14:51:37'),(148,'S002372','ALQUILER DE CERTIFICADO LAN',1,0.00,1,'2026-01-22 14:51:37'),(149,'S002373','INSTALACION Y CONFIGURACION DE AP',1,0.00,1,'2026-01-22 14:51:37'),(150,'S002374','INSTALACION Y CONFIGURACION DE SWICH ACCESO',1,0.00,1,'2026-01-22 14:51:37'),(151,'S002375','LEVANTAMIENTO DE PENDIENTES',1,0.00,1,'2026-01-22 14:51:37'),(152,'S002376','ESCALERA DOBLE ACCESO CERTIFICADA 16 PASOS',1,0.00,1,'2026-01-22 14:51:37'),(153,'S002377','ESCALERA DOBLE ACCESO CERTIFICADA 14 PASOS',1,0.00,1,'2026-01-22 14:51:37'),(154,'S002378','IMPLEMENTACION DE 3G 850MHZ, 4G 700_2600MHZ 3 SECTORES + INSTALACION DE EQUIPOS ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(155,'S002379','REUBICACION DE BBU Y HABILITACION DE RRU',1,0.00,1,'2026-01-22 14:51:37'),(156,'S002380','IMPLEMENTACION DE 3G 850MHZ, 4G 700MHZ_2600MHZ 2 SECTORES + INSTALACION DE EQUIPOS ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(157,'S002381','INSTALACION DE 1 BB DE LITIO',1,0.00,1,'2026-01-22 14:51:37'),(158,'S002382','IMPLEMENTACION DE 4G 1900MHz + INSTALACION DE EQUIPOS ENERGIA 1 SECTOR',1,0.00,1,'2026-01-22 14:51:37'),(159,'S002383','SWAP RRU 700MHz',1,0.00,1,'2026-01-22 14:51:37'),(160,'S002384','FACTIBILIDAD PARA NUEVO SUMINISTRO BT6',1,0.00,1,'2026-01-22 14:51:37'),(161,'S002385','IMPLEMENTACION DE 4G 700_2600MHz - COUBICACION B28_B7 1 SECTOR',1,0.00,1,'2026-01-22 14:51:37'),(162,'S002386','IMPLEMENTACION DE 3G 850MHZ, 4G 700MHZ 2 SECTORES + INSTALACION DE EQUIPOS ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(163,'S002387','IMPLEMENTACION DE 3G 850MHZ, 4G 700MHZ 3 SECTORES + INSTALACION DE EQUIPOS ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(164,'S002388','IMPLEMENTACION DE 4G 1900_2600MHz - COUBICACION B2_B7 1 SECTOR',1,0.00,1,'2026-01-22 14:51:37'),(165,'S002389','SWAP DE RRU S1 / INSTALACIÓN DE AAU / APT_4T4R | AWS_MM | 1900_MM',1,0.00,1,'2026-01-22 14:51:37'),(166,'S002390','LIBERAR ESPACIO EN POSTE 1 MTR',1,0.00,1,'2026-01-22 14:51:37'),(167,'S002391','ALINEAMIENTO Y MEJORAMIENTO DE CIMENTACION DE POSTE DE FO',1,0.00,1,'2026-01-22 14:51:37'),(168,'S002392','INSTALACION Y CONEXIONADO DE LUMINARIA TIPO PASTORAL A POSTE NAT',1,0.00,1,'2026-01-22 14:51:37'),(169,'S002393','SUMINISTRO E INSTALACION DE PROTECCION DE CABLES PARA POSTE NAT',1,0.00,1,'2026-01-22 14:51:37'),(170,'S002394','SUMINISTRO E INSTALACION DE POSTE',1,0.00,1,'2026-01-22 14:51:37'),(171,'S002395','INSTALACION DE GABINETE',1,0.00,1,'2026-01-22 14:51:37'),(172,'S002396','SUMINISTRO E INSTALACION DE MURETE, TABLERO Y REJILLA',1,0.00,1,'2026-01-22 14:51:37'),(173,'S002418','INSTALACION DE TABLERO ELECTRICO',1,0.00,1,'2026-01-22 14:51:37'),(174,'S002419','SUMINISTRO E INSTALACION DE PUERTA DE ACERO GALVANIZADO',1,0.00,1,'2026-01-22 14:51:37'),(175,'S002420','SUMINISTRO E INSTALACION DE PUERTA 1.104 x 2.1 MTSDE ACERO GALVANIZADO',1,0.00,1,'2026-01-22 14:51:37'),(176,'S002421','ESTANTERIA ANGULO RANURADO DE 11/2 X 11/2 X 2 MM: 2.00M X 1.13M X 0.60M 4 NIVELES COLOR GRIS',1,0.00,1,'2026-01-22 14:51:37'),(177,'S002422','INSTALACION DE ESTANTE',1,0.00,1,'2026-01-22 14:51:37'),(178,'S002423','IMPLEMENTACION DE NODO',1,0.00,1,'2026-01-22 14:51:37'),(179,'S002424','IMPLEMENTACION DE 4G 700MHz - COUBICACION B28 1 SECTOR',1,0.00,1,'2026-01-22 14:51:37'),(180,'S002425','REFORZAMIENTO DE POSTE + INSTALACION DE SOPORTES + OBRA CIVIL',1,0.00,1,'2026-01-22 14:51:37'),(181,'S002426','ADECUACION DE SITIO + SUMINISTRO E INSTALACION DE SOPORTES + OBRA CIVIL Y ELECTRICA',1,0.00,1,'2026-01-22 14:51:37'),(182,'S002427','GESTION DE NUEVO SUMINSTRO',1,0.00,1,'2026-01-22 14:51:37'),(183,'S002428','SERVICIO DE SUPERVISION DE OBRAS ELECTRICAS Y OBRAS DE AMPLIACION POR DEMANDA',1,0.00,1,'2026-01-22 14:51:37'),(184,'S002429','ALQUILER DE ANDAMIO COLGANTE CON OPERARIO',1,0.00,1,'2026-01-22 14:51:37'),(185,'S002430','INSTALACION DE ANDAMIO COLGANTE',1,0.00,1,'2026-01-22 14:51:37'),(186,'S002431','SERVICIO DE EVALUACION ESTRUCTURAL DE TORRE EXISTENTE Y REFORZAMIENTO DE TORRE Y EDIFICACION',1,0.00,1,'2026-01-22 14:51:37'),(187,'S002432','RELEVAMIENTO DE EDIFICACION Y EVALUACION ESTRUCTURAL',1,0.00,1,'2026-01-22 14:51:37'),(188,'S002433','GESTIÓN E INDEPENDIZACIÓN DE CARGA',1,0.00,1,'2026-01-22 14:51:37'),(189,'S002434','OBRAS DE CONSTRUCCION CIVILES PARA INSTALACION DE FIBRA OPTICA INCLUYE MATERIALES',1,0.00,1,'2026-01-22 14:51:37'),(190,'S002435','IMPLEMENTACION DE 4G 1900_2600MHZ 2 SECTORES + INSTALACION DE EQUIPOS ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(191,'S002436','JORNAL DE OPERARIO',1,0.00,1,'2026-01-22 14:51:37'),(192,'S002437','SUMINISTRO E INSTALACION DE MURETE INCLUYE MATERIALES',1,0.00,1,'2026-01-22 14:51:37'),(193,'S002438','SUMINISTRO E INSTALACION DE CABLE CAAI 2 X 35MM2',1,0.00,1,'2026-01-22 14:51:37'),(194,'S002439','SERVICIO DE CONSTRUCCION DE ENCAMISADO, REFORZADO E IMPERMEABILIZADO DE POSTE 18 METROS',1,0.00,1,'2026-01-22 14:51:37'),(195,'S002440','INSTALACION DE BATERIA',1,0.00,1,'2026-01-22 14:51:37'),(196,'S002441','IMPLEMENTACION DE ANTENA STARLINK',1,0.00,1,'2026-01-22 14:51:37'),(197,'S002442','SERVICIO DE TRANSPORTE Y EMBALAJE',1,0.00,1,'2026-01-22 14:51:37'),(198,'S002443','IMPLEMENTACION DE 3G 850MHZ, 4G 700MHZ_1900MHZ 2 SECTORES + INSTALACION DE EQUIPOS ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(199,'S002444','CAMBIO DE ACOMETIDA',1,0.00,1,'2026-01-22 14:51:37'),(200,'S002445','AMPLIACION DE POTENCIA BT6',1,0.00,1,'2026-01-22 14:51:37'),(201,'S002446','INSTALACION DE PUERTA DE ACERO',1,0.00,1,'2026-01-22 14:51:37'),(202,'S002447','DESMONTAJE DE TORRE Y OBRA CIVIL',1,0.00,1,'2026-01-22 14:51:37'),(203,'S002448','CANALIZADO SUBTERRANEO DE LA RED DE BAJA TENSION HACIA EL MURETE',1,0.00,1,'2026-01-22 14:51:37'),(204,'S002449','ADECUACION DE INCREMENTO DE CARGA',1,0.00,1,'2026-01-22 14:51:37'),(205,'S002450','CONSULTARIA EN SIG PARA AUDITORIA',1,0.00,1,'2026-01-22 14:51:37'),(206,'S002451','REPARACION DE PUERTA CONTRAPLACADA',1,0.00,1,'2026-01-22 14:51:37'),(207,'S002452','REPARACION E INSTALACION DE PUERTA DE FIBRA',1,0.00,1,'2026-01-22 14:51:37'),(208,'S002453','SWAP RRU DUAL BAND 700-850MHz 1 SECTOR',1,0.00,1,'2026-01-22 14:51:37'),(209,'S002454','IMPLEMENTACION DE 3G 850MHZ, 4G 700_2600MHZ - COUBICACION B5_B28_B7 3 SECTORES',1,0.00,1,'2026-01-22 14:51:37'),(210,'S002455','IMPLEMENTACION DE 4G 700_2600MHz - COUBICACION B28_B7 2 SECTORES',1,0.00,1,'2026-01-22 14:51:37'),(211,'S002456','LEVANTAMIENTO DE OBSERVACIONES DE ATP',1,0.00,1,'2026-01-22 14:51:37'),(212,'S002457','SUMINISTRO E INSTALACION DE EQUIPO DE AIRE ACONDICIONADO DE 60 000 BTU - TIPO SPLIT DUCTO, R-410 CONVECIONAL MARCA MIDEA',1,0.00,1,'2026-01-22 14:51:37'),(213,'S002458','SUMINISTRO E INSTALACION DE ACOMETIDA DE TABLERO ELECTRICO',1,0.00,1,'2026-01-22 14:51:37'),(214,'S002459','INSTALACION DE GPS + TARJETAS',1,0.00,1,'2026-01-22 14:51:37'),(215,'S002460','SUMINISTRO E INSTALACION DE LUZ DE EMERGENCIA',1,0.00,1,'2026-01-22 14:51:37'),(216,'S002461','SUMINISTRO E INSTALACION DE ENERGIA PROVISIONAL',1,0.00,1,'2026-01-22 14:51:37'),(217,'S002462','MANTENIMIENTO CORRECTIVO DEL SISTEMA DEL ALARMA CONTRAINCENDIO',1,0.00,1,'2026-01-22 14:51:37'),(218,'S002463','MANTENIMIENTO CORRECTIVO DEL TABLERO ELECTRICOTOMACORRIENTE COMPUTO',1,0.00,1,'2026-01-22 14:51:37'),(219,'S002464','MANTENIMIENTO CORRECTIVO DEL TABLERO GENERAL TG',1,0.00,1,'2026-01-22 14:51:37'),(220,'S002465','MANTENIMIENTO DE POZO A TIERRA',1,0.00,1,'2026-01-22 14:51:37'),(221,'S002466','CANALIZADO SUBTERRANEO',1,0.00,1,'2026-01-22 14:51:37'),(222,'S002467','SUMINISTRO E INSTALACION DE POSTES TUBULARES P/TENDIDO DE ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(223,'S002468','IMPLEMENTACION DE 5G - COUBICACION B78 3 SECTORES',1,0.00,1,'2026-01-22 14:51:37'),(224,'S002469','MIGRACION DE SERVICIOS',1,0.00,1,'2026-01-22 14:51:37'),(225,'S002470','SERVICIO DE IMPLEMENTACION DE FIBRA OPTICA',1,0.00,1,'2026-01-22 14:51:37'),(226,'S002471','SERVICIO DE ENTREGABLES DE PROYECTO',1,0.00,1,'2026-01-22 14:51:37'),(227,'S002472','SEGUIMIENTO Y MONITOREO PARA ENTREGABLES',1,0.00,1,'2026-01-22 14:51:37'),(228,'S002473','REPLANTEO Y ACTUALIZACIÓN DE PLANOS',1,0.00,1,'2026-01-22 14:51:37'),(229,'S002474','CONFIGURACION DE BBU',1,0.00,1,'2026-01-22 14:51:37'),(230,'S002475','INSTALACION DE ODF',1,0.00,1,'2026-01-22 14:51:37'),(231,'S002476','SERVICIO CONSULTORIA COMERCIAL',1,0.00,1,'2026-01-22 14:51:37'),(232,'S002477','INSTALACION DE CAJA NAP',1,0.00,1,'2026-01-22 14:51:37'),(233,'S002478','ROTULADO DE CAJA NAP',1,0.00,1,'2026-01-22 14:51:37'),(234,'S002479','SUPERVISION DE OBRAS ELECTRICAS Y OBRAS DE AMPLIACION POR DEMANDA',1,0.00,1,'2026-01-22 14:51:37'),(235,'S002480','SERVICIO DE OBRA CIVIL Y REFORZAMIENTO DE POSTE',1,0.00,1,'2026-01-22 14:51:37'),(236,'S002481','SERVICIO DE OBRA CIVIL, TRASLADO E IZAJE DE POSTE',1,0.00,1,'2026-01-22 14:51:37'),(237,'S002482','INSTALACION DE MUFA DE DISTRIBUCION',1,0.00,1,'2026-01-22 14:51:37'),(238,'S002483','MEDICION DE TRAZAS FIBRA OPTICA CON EQUIPO OTDR',1,0.00,1,'2026-01-22 14:51:37'),(239,'S002484','MEDICION DE POTENCIA CON POWER METER',1,0.00,1,'2026-01-22 14:51:37'),(240,'S002485','SERVICIO DE TRAMITE DOCUMENTARIO',1,0.00,1,'2026-01-22 14:51:37'),(241,'S002486','SERVICIO DE ACABADOS TRABAJOS DE PLANTA EXTERNA',1,0.00,1,'2026-01-22 14:51:37'),(242,'S002487','CORRECCION DE CAJAS NAP',1,0.00,1,'2026-01-22 14:51:37'),(243,'S002488','ACONDICIONAMIENTO DE EMPALMES DE CAJAS NAP',1,0.00,1,'2026-01-22 14:51:37'),(244,'S002489','CUADRILLA DE AVERIAS Y ALQUILER DE EQUIPOS',1,0.00,1,'2026-01-22 14:51:37'),(245,'S002490','GESTION DOCUMENTARIA Y NUEVO SUMINISTRO',1,0.00,1,'2026-01-22 14:51:37'),(246,'S002491','ACARREO Y DIAS PARALIZADOS',1,0.00,1,'2026-01-22 14:51:37'),(247,'S002492','REMODELACION COMEDOR SEDE ARRIOLA',1,0.00,1,'2026-01-22 14:51:37'),(248,'S002493','TRABAJO CORRECTIVO DEL SISTEMA DE DETECCION Y ALARMA CONTRA INCENDIOS',1,0.00,1,'2026-01-22 14:51:37'),(249,'S002494','EXCAVACION Y MOVILIZACION DE GRUA',1,0.00,1,'2026-01-22 14:51:37'),(250,'S002495','INSTALACION DE EQUIPOS RF Y ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(251,'S002496','DEMOLICION DE ESTRUCTURAS DE CONCRETO',1,0.00,1,'2026-01-22 14:51:37'),(252,'S002497','DEMOLICION DE CIMENTACION',1,0.00,1,'2026-01-22 14:51:37'),(253,'S002498','REUBICACION DE FIBRA OPTICA INCLUYE MATERIALES',1,0.00,1,'2026-01-22 14:51:37'),(254,'S002499','DESMONTAJE DE POSTE',1,0.00,1,'2026-01-22 14:51:37'),(255,'S002500','CERTIFICACION DE PRUEBAS MAAT FO',1,0.00,1,'2026-01-22 14:51:37'),(256,'S002501','INSTALACION, FUSION Y ACONDICIONADO DE MUFA DISTRIBUCION 1 A 144 HILOS',1,0.00,1,'2026-01-22 14:51:37'),(257,'S002502','INSTALACION, FUSION Y ACONDICIONADO DE MUFA DISTRIBUCION 1 A 96 HILOS',1,0.00,1,'2026-01-22 14:51:37'),(258,'S002503','TENDIDO DE FIBRA OPTICA 24 HILOS',2,0.00,1,'2026-01-22 14:51:37'),(259,'S002504','TENDIDO DE FIBRA OPTICA 96 HILOS',2,0.00,1,'2026-01-22 14:51:37'),(260,'S002505','TRAMITE ANTE CONCESIONARIA PARA ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(261,'S002506','EVALUACION ESTRUCTURAL',1,0.00,1,'2026-01-22 14:51:37'),(262,'S002507','ALQUILER DE GRUPO ELECTROGENO',1,0.00,1,'2026-01-22 14:51:37'),(263,'S002508','INSTALACION DE TORRE TRUCK + SOPORTE Y OBRA CIVIL',1,0.00,1,'2026-01-22 14:51:37'),(264,'S002509','DESMONTAJE DE TORRE TRUCK',1,0.00,1,'2026-01-22 14:51:37'),(265,'S002510','INSTALACION DE POSTE EN VEREDA',1,0.00,1,'2026-01-22 14:51:37'),(266,'S002511','TENDIDO DE CABLE AUTOPORTANTE INCLUYE MATERIALES',2,0.00,1,'2026-01-22 14:51:37'),(267,'S002512','GESTION Y SUMINISTRO PARA INSTALACION DE ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(268,'S002513','MANTENIMIENTO CORRECTIVO DEL POZO PUESTA A TIERRA',1,0.00,1,'2026-01-22 14:51:37'),(269,'S002514','ACTUALIZACIÓN DE PLANO DE ARQUITECTURA CON DISTRIBUCIÓN EXISTENTE Y DETALLE DE CÁLCULO DE AFORO',1,0.00,1,'2026-01-22 14:51:37'),(270,'S002515','CERTIFICADO DE RETARDANTE A LA LLAMA DE LA ALFOMBRA',1,0.00,1,'2026-01-22 14:51:37'),(271,'S002516','CERTIFICADO DE LA SEGURIDAD DE LOS VIDRIOS EXISTENTES',1,0.00,1,'2026-01-22 14:51:37'),(272,'S002517','VERIFICACIÓN DEL SISTEMA DE DETECCIÓN Y ALARMA DE INCENDIOS',1,0.00,1,'2026-01-22 14:51:37'),(273,'S002518','VERIFICACIÓN DEL SISTEMA DE LAS LUCES DE EMERGENCIA',1,0.00,1,'2026-01-22 14:51:37'),(274,'S002519','VERIFICACIÓN DEL SISTEMA ELÉCTRICO, PRINCIPALMENTE EN LOS TABLEROS ELÉCTRICOS Y LOS CIRCUITOS PREDOMINANTES SOBRE EL FALSO CIELO RASO',1,0.00,1,'2026-01-22 14:51:37'),(275,'S002520','TENDIDO DE FIBRA OPTICA 144 HILOS',2,0.00,1,'2026-01-22 14:51:37'),(276,'S002521','REALIEACION Y VALIDACION DE ENLACE MW',1,0.00,1,'2026-01-22 14:51:37'),(277,'S002522','ELABORACION DE ESTUDIO DE APANTALLAMIENTO PARA LA DGAC',1,0.00,1,'2026-01-22 14:51:37'),(278,'S002523','ELABORACION DE ESTUDIO AERONAUTICO PARA LA DGAC',1,0.00,1,'2026-01-22 14:51:37'),(279,'S002524','SEGUIMIENTO DE EXPEDIENTES PARA LA AUTORIZACIÓN DE INSTALACIÓN DE MURETE',1,0.00,1,'2026-01-22 14:51:37'),(280,'S002525','INSTALACION DE CRUCETA PARA RESERVA DE FIBRA OPTICA',1,0.00,1,'2026-01-22 14:51:37'),(281,'S002526','INSTALACION DE MENSAJERO GALVANIZADO',1,0.00,1,'2026-01-22 14:51:37'),(282,'S002527','HABILITACIÓN PARA LA INSTALACIÓN DE SUMINISTRO ELECTRICO TRIFASICO',1,0.00,1,'2026-01-22 14:51:37'),(283,'S002528','SERVICIO DE CANALIZADO INCLUYE MATERIALES',1,0.00,1,'2026-01-22 14:51:37'),(284,'S002529','GESTION DE FIRMA DE CARTA DE AUTORIZACION',1,0.00,1,'2026-01-22 14:51:37'),(285,'S002530','SUMINISTRO E INSTALACION DE CAJA METALICA Y SISTEMA DE PUESTA A TIERRA',1,0.00,1,'2026-01-22 14:51:37'),(286,'S002531','SUMINISTRO E INSTALACION DE MASTIL + CUELLO DE GANSO',1,0.00,1,'2026-01-22 14:51:37'),(287,'S002532','EXCAVACION DE ZANJA PARA PLANTADO DE MURETE',1,0.00,1,'2026-01-22 14:51:37'),(288,'S002533','CABLEADO DESDE TABLERO A MURETE',1,0.00,1,'2026-01-22 14:51:37'),(289,'S002534','SUMINISTRO E INSTALACION DE REJILLA Y CANDADO',1,0.00,1,'2026-01-22 14:51:37'),(290,'S002535','APOYO EN LA GESTION CON LA CONCESIONARIA',1,0.00,1,'2026-01-22 14:51:37'),(291,'S002536','SERVICIO DE CONSULTORIA EN TELECOMUNICACIONES',1,0.00,1,'2026-01-22 14:51:37'),(292,'S002537','SUMINISTRO E INSTALACION DE PUESTA A TIERRA, CAJA F1, PORTAMEDIDOR Y LLAVE DE FUERZA + DEMOLICION, RESANE DE PARED Y VEREDA',1,0.00,1,'2026-01-22 14:51:37'),(293,'S002538','SUMINISTRO E INSTALACION DE VENTILADOR EN LINEA DE TRANSMISION DIRECTA CENTRIFUGO JETLINE-200 MONOFASICO 220V',1,0.00,1,'2026-01-22 14:51:37'),(294,'S002539','SUMINISTRO E INSTALACION DE PUESTA A TIERRA, CAJA F1, PORTAMEDIDOR Y LLAVE DE FUERZA + CABLE ACOMETIDA + PIEDRA LAJA Y APLICACIÓN DE BARNIZ + DEMOLICION, RESANE DE PARED Y VEREDA',1,0.00,1,'2026-01-22 14:51:37'),(295,'S002540','REPARACIÓN E INSTALACIÓN DE ESCALERA Y BRIDAS DE SOPORTE PARA MONOPOLO + SERVICIO DE CORTE DE PERNOS Y HUECOS',1,0.00,1,'2026-01-22 14:51:37'),(296,'S002541','SUMINISTRO E INSTALACION DE EQUIPO DE INYECCION DE AIRE TD-500',1,0.00,1,'2026-01-22 14:51:37'),(297,'S002542','INSTALACION DE MENSAJERO, CRUCETA, POWER METER',1,0.00,1,'2026-01-22 14:51:37'),(298,'S002543','SUMINISTRO E INSTALACION DE MURETE, REJILLA CON CANDADO, CAJA F1, PORTAMEDIDOR, MASTIL GALVANISADO Y TABLERO + DEMOLICION, RESANE DE PARED Y VEREDA',1,0.00,1,'2026-01-22 14:51:37'),(299,'S002544','DESMONTAJE DE POLIMERO Y ESTRUCTURAS DE RADOMO',1,0.00,1,'2026-01-22 14:51:37'),(300,'S002545','SERVICIO DE HOSTING WEB SSD + REGISTRO DE DOMINIO WEB',1,0.00,1,'2026-01-22 14:51:37'),(301,'S002546','CALIBRACION DE BRUJULA',1,0.00,1,'2026-01-22 14:51:37'),(302,'S002547','CALIBRACION DE INCLINOMETRO',1,0.00,1,'2026-01-22 14:51:37'),(303,'S002548','CALIBRACION DE PINZA MULTIMETRICA',1,0.00,1,'2026-01-22 14:51:37'),(304,'S002549','LEVANTAMIENTO DE OBSERVACIONES FTA',1,0.00,1,'2026-01-22 14:51:37'),(305,'S002550','SERVICIO DE MOVILIDAD',1,0.00,1,'2026-01-22 14:51:37'),(306,'S002551','OBSTRUCCION DE DUCTOS',1,0.00,1,'2026-01-22 14:51:37'),(307,'S002552','CONSTRUCCION DE DUCTOS DE VENTILACION',1,0.00,1,'2026-01-22 14:51:37'),(308,'S002553','ELABORACION DE ESTUDIO PAMA',1,0.00,1,'2026-01-22 14:51:37'),(309,'S002554','ELABORACION DE EXPEDIENTE Y TRAMITE SERNANP',1,0.00,1,'2026-01-22 14:51:37'),(310,'S002555','SUMINISTRO E INSTALACION DE MURETE CON LINEAS DE CARGA',1,0.00,1,'2026-01-22 14:51:37'),(311,'S002556','DESMONTAJE DE EXTRACTOR',1,0.00,1,'2026-01-22 14:51:37'),(312,'S002557','INSTALACION DE SISTEMA DE EXTRACCION HELICOCENTRIFUGO',1,0.00,1,'2026-01-22 14:51:37'),(313,'S002558','ELABORACION DE ESTUDIO EVAP',1,0.00,1,'2026-01-22 14:51:37'),(314,'S002559','MANTENIMIENTO DE PLATAFORMA DE TRABAJO Y FABRICACIÓN DE BARANDAS',1,0.00,1,'2026-01-22 14:51:37'),(315,'S002560','SERVICIO DE ARENADO, GALVANIZADO Y REPARACIÓN DE MODULOS DE MONOPOLO H=6M',1,0.00,1,'2026-01-22 14:51:37'),(316,'S002561','OTROS GASTOS',1,0.00,1,'2026-01-22 14:51:37'),(317,'S002562','CONSTRUCCION DE ZAPATA Y COLUMNA',1,0.00,1,'2026-01-22 14:51:37'),(318,'S002563','ENZAMBLAR POSTE GALV E IZAR A MANIOBRA',1,0.00,1,'2026-01-22 14:51:37'),(319,'S002564','IZAJE DE POSTE GALV',1,0.00,1,'2026-01-22 14:51:37'),(320,'S002565','EXCAVACION, TRANSPORTE E INSTALACION',1,0.00,1,'2026-01-22 14:51:37'),(321,'S002566','SUMINISTRO E INSTALACION DE MURETE + CABLEADO P/MEDIDOR',1,0.00,1,'2026-01-22 14:51:37'),(322,'S002567','ADECUACION DE MECHA P/INSTALACION DE MEDIDOR',1,0.00,1,'2026-01-22 14:51:37'),(323,'S002568','BASAMENTO DE 0.80X0.80X1.50 MT P/POSTE',1,0.00,1,'2026-01-22 14:51:37'),(324,'S002569','INSTALACION DE POSTE PRFV',1,0.00,1,'2026-01-22 14:51:37'),(325,'S002570','INSTALACION DE MODULO RECTIFICADOR',1,0.00,1,'2026-01-22 14:51:37'),(326,'S002571','INSTALACION DE POSTE CAC 11 MTS',1,0.00,1,'2026-01-22 14:51:37'),(327,'S002572','SUMINISTRO E INSTALACION DE REJILLA Y CANDADO, CONEXIONADO DE CABLE A NUEVO TABLERO',1,0.00,1,'2026-01-22 14:51:37'),(328,'S002573','ALQUILER DE ANDAMIO',1,0.00,1,'2026-01-22 14:51:37'),(329,'S002574','ARMADO Y DESARMADO DE ANDAMIOS',1,0.00,1,'2026-01-22 14:51:37'),(330,'S002575','INSTALACIION DE NUEVA CANALETA Y ESCALERILLA + RETIRO DE CANALETA Y CABLE ANTIGUO',1,0.00,1,'2026-01-22 14:51:37'),(331,'S002576','MANTENIMIENTO PREVENTIVO A EQUIPO DE AIRE ACONDICIONADO',1,0.00,1,'2026-01-22 14:51:37'),(332,'S002577','SUMINISTRO E INSTALACION DE 50 MTS DE CABLE AC + CAMBIO DE TABLERO',1,0.00,1,'2026-01-22 14:51:37'),(333,'S002578','REUBICACION DE BANDEJA FO',1,0.00,1,'2026-01-22 14:51:37'),(334,'S002579','SERVICIO DE CAPACITACION',1,0.00,1,'2026-01-22 14:51:37'),(335,'S002580','ACONDICIONAMIENTO DE CAJAS NAP Y MUFA',1,0.00,1,'2026-01-22 14:51:37'),(336,'S002581','COLOCACIÓN DE BRAZO',1,0.00,1,'2026-01-22 14:51:37'),(337,'S002582','DEVANADO DE MEDIO TRAMO',1,0.00,1,'2026-01-22 14:51:37'),(338,'S002583','FUSION DE CAJA NAP, MUFA Y EMPALMES',1,0.00,1,'2026-01-22 14:51:37'),(339,'S002584','TENDIDO DE FO AEREO',2,0.00,1,'2026-01-22 14:51:37'),(340,'S002585','DESMONTAJE DE GABINETE',1,0.00,1,'2026-01-22 14:51:37'),(341,'S002586','SUMINISTRO E INSTALACION DE SOPORTES RF',1,0.00,1,'2026-01-22 14:51:37'),(342,'S002587','TRABAJO DE MANTENIMIENTO Y REPARACION DE DUCTOS',1,0.00,1,'2026-01-22 14:51:37'),(343,'S002588','REFORZAMIENTO DE CIMENTACION DE POSTE',1,0.00,1,'2026-01-22 14:51:37'),(344,'S002589','INSTALACION DE ENERGIA PROVISIONAL',1,0.00,1,'2026-01-22 14:51:37'),(345,'S002590','INSTALACION DE ACOMETIDA EN PUNTO DE FACTIBILIDAD',1,0.00,1,'2026-01-22 14:51:37'),(346,'S002591','INSTALACION  GPS',1,0.00,1,'2026-01-22 14:51:37'),(347,'S002592','SWAP DE BBU + INSTALACION GPS',1,0.00,1,'2026-01-22 14:51:37'),(348,'S002593','UPGRADE BBU',1,0.00,1,'2026-01-22 14:51:37'),(349,'S002594','ADECUACION DE ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(350,'S002595','NODO OVERLAP PRECO INCLUYE TRONCAL',4,0.00,1,'2026-01-22 14:51:37'),(351,'S002596','ADECUACION DE CANALIZADO PARA ACOMETIDA',1,0.00,1,'2026-01-22 14:51:37'),(352,'S002597','DESMONTAJE MW',1,0.00,1,'2026-01-22 14:51:37'),(353,'S002598','PERSONAL PARA FABRICACION DE SOPORTE',1,0.00,1,'2026-01-22 14:51:37'),(354,'S002599','SUMINISTRO DE KIT DE ANCLAJE DE ACERO',1,0.00,1,'2026-01-22 14:51:37'),(355,'S002600','PLANTADO DE ANCLAJE PARA POSTE DE 9 MTS',1,0.00,1,'2026-01-22 14:51:37'),(356,'S002601','CONSTRUCCION DE BLOQUE DE CONCRETO PARA ARMADO DE UN ANCLAJE DE ACERO PARA POSTE',1,0.00,1,'2026-01-22 14:51:37'),(357,'S002602','AMPLIACION DE CAPACIDAD ENLACE MW',1,0.00,1,'2026-01-22 14:51:37'),(358,'S002603','SWAP DE BATERIA',1,0.00,1,'2026-01-22 14:51:37'),(359,'S002604','INSTALACION DE ROUTER + UPGRADE BBU',1,0.00,1,'2026-01-22 14:51:37'),(360,'S002605','SWAP DE GABINETE',1,0.00,1,'2026-01-22 14:51:37'),(361,'S002606','GASTOS DE EPPS + CACHACOS + SERVICIO DE TRANSPORTE',1,0.00,1,'2026-01-22 14:51:37'),(362,'S002607','SUMINISTRO E INSTALACION DE COPA P/POSTE DE CONCRETO',1,0.00,1,'2026-01-22 14:51:37'),(363,'S002608','CONVERSION DE RECTIFICADOR DE MONOFASICO A TRIFASICO, BALANCEO, ROTULADO Y ETIQUETADO',1,0.00,1,'2026-01-22 14:51:37'),(364,'S002609','PINTURA CON ACABADO EPOXICO EN MASTIL Y SOPORTES',1,0.00,1,'2026-01-22 14:51:37'),(365,'S002610','CONSTRUCCION DE PEDESTAL Y DADO DE CONCRETO',1,0.00,1,'2026-01-22 14:51:37'),(366,'S002611','CANALIZADO CON TUBERIA CONDUIT',1,0.00,1,'2026-01-22 14:51:37'),(367,'S002612','ELABORACION DE EXPEDIENTE DE ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(368,'S002613','INSTALACION DE TRAFORMADOR ELEVADOR/REDUCTOR',1,0.00,1,'2026-01-22 14:51:37'),(369,'S002614','REFORZAMIENTO DE ACOMETIDA',1,0.00,1,'2026-01-22 14:51:37'),(370,'S002615','RESPALDO TECNICO DE ESTRUCTURA Y ENERGIA',1,0.00,1,'2026-01-22 14:51:37'),(371,'S002616','CIMENTACION DE LA BASE DE POSTE',1,0.00,1,'2026-01-22 14:51:37'),(372,'S002617','INSTALACION DE RETENIDAS EN POSTE',1,0.00,1,'2026-01-22 14:51:37'),(373,'S002618','INSTALACION DE MASTIL Y SOPORTES + OBRA CIVIL',1,0.00,1,'2026-01-22 14:51:37'),(374,'S002619','ADECUACION RESERVA DE CABLE DE POSTE Y CONEXIÓN EN TABLERO',1,0.00,1,'2026-01-22 14:51:37'),(375,'S002620','DESMONTAJE DE RTN MW',1,0.00,1,'2026-01-22 14:51:37'),(376,'S002621','MODIFICACION DE SOPORTE RF',1,0.00,1,'2026-01-22 14:51:37'),(377,'C00001','BRAZO RF MASTIL A TUBO 50 CM',5,0.00,1,'2026-01-22 14:51:37'),(378,'C00002','BRAZO RF MASTIL A TUBO 70 CM',5,0.00,1,'2026-01-22 14:51:37'),(379,'C00003','BREAKER UNIPOLAR 63 AMP',1,0.00,1,'2026-01-22 14:51:37'),(380,'C00004','CAJA PANDUIT DE 6 SALIDAS',1,0.00,1,'2026-01-22 14:51:37'),(381,'C00005','CINTA AISLANTE AMARILLA',1,0.00,1,'2026-01-22 14:51:37'),(382,'C00006','CINTA AISLANTE AZUL',1,0.00,1,'2026-01-22 14:51:37'),(383,'C00007','CINTA AISLANTE BLANCA',1,0.00,1,'2026-01-22 14:51:37'),(384,'C00008','CINTA AISLANTE ROJA',1,0.00,1,'2026-01-22 14:51:37'),(385,'C00009','CINTILLOS CV 300 BLANCO',6,0.00,1,'2026-01-22 14:51:37'),(386,'C00010','CINTILLOS CV 300 NEGRO',6,0.00,1,'2026-01-22 14:51:37'),(387,'C00011','GRASA CONDUCTIVA',7,0.00,1,'2026-01-22 14:51:37'),(388,'C00012','PATCH CORD F.O. INDOOR LC/UPC-SC/UPC 3MTS',1,0.00,1,'2026-01-22 14:51:37'),(389,'C00013','PATCH CORD F.O. INDOOR LC/UPC-SC/UPC 5MTS',1,0.00,1,'2026-01-22 14:51:37'),(390,'C00014','PATCH CORD F.O. OUTDOOR LC/UPC-SC/UPC 3MTS',1,0.00,1,'2026-01-22 14:51:37'),(391,'C00015','PRENSA ESTOPA METALICA 1°',1,0.00,1,'2026-01-22 14:51:37'),(392,'C00016','PRENSA ESTOPA METALICA 3/4°',1,0.00,1,'2026-01-22 14:51:37'),(393,'C00017','SIKAFLEX 40FC',1,0.00,1,'2026-01-22 14:51:37'),(394,'C00018','BRAZO RF ROSETA A TUBO 50 CM',5,0.00,1,'2026-01-22 14:51:37'),(395,'C00019','SOPORTE ROSETA 60 CM',5,0.00,1,'2026-01-22 14:51:37'),(396,'C00020','TERMINAL T/OJO 16-8',1,0.00,1,'2026-01-22 14:51:37'),(397,'C00021','TERMINAL T/OJO 25-8',1,0.00,1,'2026-01-22 14:51:37'),(398,'C00022','TERMINAL T/PUNTA 16',1,0.00,1,'2026-01-22 14:51:37'),(399,'C00023','TERMINAL T/PUNTA 25',1,0.00,1,'2026-01-22 14:51:37'),(400,'C00024','TUBO CORRUGADO PESADO 1°',2,0.00,1,'2026-01-22 14:51:37'),(401,'C00025','TUBO CORRUGADO PESADO 3/4°',2,0.00,1,'2026-01-22 14:51:37'),(402,'C00026','TUBO ESPIRAL PROTECTOR DE CABLES',2,0.00,1,'2026-01-22 14:51:37'),(403,'C00027','TUBO RF 2.5° X 2 MTS',1,0.00,1,'2026-01-22 14:51:37'),(404,'C00028','AMARRE PREFORMADO AMARILLO 1/4',1,0.00,1,'2026-01-22 14:51:37'),(405,'C00029','CIERRE DE EMPALME VERTICAL 96 HILOS',1,0.00,1,'2026-01-22 14:51:37'),(406,'C00030','TRANSFORMADOR DE AISLAMIENTO TRIFASICO EN SECO DE 25 KVA',1,0.00,1,'2026-01-22 14:51:37'),(407,'C00031','AMARRE PREFORMADO AZUL 1/2',1,0.00,1,'2026-01-22 14:51:37'),(408,'C00032','BRAZOS EXTENSOR DE 60 CM CP 1/2',1,0.00,1,'2026-01-22 14:51:37'),(409,'C00033','CABLE DE ACERO TIPO RETENIDA 1/4',2,0.00,1,'2026-01-22 14:51:37'),(410,'C00034','CANALETA O PROTECTOR U',1,0.00,1,'2026-01-22 14:51:37'),(411,'C00035','CLAMP DE SUSPENION DE 3H BOLT',1,0.00,1,'2026-01-22 14:51:37'),(412,'C00036','CLEVIS CON AISLADOR',1,0.00,1,'2026-01-22 14:51:37'),(413,'C00037','CRUCETA PARA RESERVA DE 80CM ESTANDAR',1,0.00,1,'2026-01-22 14:51:37'),(414,'C00038','ETIQUETA CLARO PARA PEXT',1,0.00,1,'2026-01-22 14:51:37'),(415,'C00039','FLEJE DE ACERO INOX 1/2 x 30 MT BANDING',1,0.00,1,'2026-01-22 14:51:37'),(416,'C00040','FLEJE DE ACERO INOX 3/4 x 30 MT BANDING',1,0.00,1,'2026-01-22 14:51:37'),(417,'C00041','HEBILLA 1/2 BANDING',1,0.00,1,'2026-01-22 14:51:37'),(418,'C00042','HEBILLA 3/4 BANDING',1,0.00,1,'2026-01-22 14:51:37'),(419,'C00043','KIT DE SUSPENSION ADSS',1,0.00,1,'2026-01-22 14:51:37'),(420,'C00044','ALAMBRE DEBANAR',8,0.00,1,'2026-01-22 14:51:37'),(421,'C00045','ABRAZADERA TIPO U DE 1/2°',1,0.00,1,'2026-01-22 14:51:37'),(422,'C00046','CINTILLO BANDERITA',1,0.00,1,'2026-01-22 14:51:37'),(423,'C00047','CINTILLOS CV 100 NEGRO',1,0.00,1,'2026-01-22 14:51:37'),(424,'C00048','SIKABOOM',1,0.00,1,'2026-01-22 14:51:37'),(425,'C00049','SILICONA NEGRA',1,0.00,1,'2026-01-22 14:51:37'),(426,'C00050','TARUGO VERDE DE PLASTICO',1,0.00,1,'2026-01-22 14:51:37'),(427,'C00051','TUBO CORRUGADO PESADO 1/2°',1,0.00,1,'2026-01-22 14:51:37'),(428,'C00052','ACOPLADOR DUPLEX SC PANDUIT',1,0.00,1,'2026-01-22 14:51:37'),(429,'C00053','PIGTAIL SM SC/UPC 1M',1,0.00,1,'2026-01-22 14:51:37'),(430,'C00054','TUBO RF 2° X 3 MTS',1,0.00,1,'2026-01-22 14:51:37'),(431,'C00055','PATCH CORD F.O. OUTDOOR LC/UPC-SC/UPC 7MTS',1,0.00,1,'2026-01-22 14:51:37'),(432,'C00056','BRAZO RF TUBO A TUBO 60 CM',5,0.00,1,'2026-01-22 14:51:37'),(433,'C00057','BRAZO RF TUBO A UBOLT 60 CM',5,0.00,1,'2026-01-22 14:51:37'),(434,'C00058','BRAZO RF P/TORRE AUTOSOPORTADA FIJO Y REGULABLE 40 CM',5,0.00,1,'2026-01-22 14:51:37'),(435,'C00059','BRAZO RF P/TORRE VENTADA 25 CM CON ABRAZADERA',5,0.00,1,'2026-01-22 14:51:37'),(436,'C00060','BRAZO RF TUBO A TUBO 70 CM',5,0.00,1,'2026-01-22 14:51:37'),(437,'C00061','BRAZO RF TUBO A UBOLT 70 CM',5,0.00,1,'2026-01-22 14:51:37'),(438,'C00062','BRAZOS EXTENSOR DE 80 CM CP 1/2',1,0.00,1,'2026-01-22 14:51:37'),(439,'C00063','AMARRE PREFORMADO NARANJA 3/8',1,0.00,1,'2026-01-22 14:51:37'),(440,'C00064','PATCH CORD F.O. INDOOR LC/UPC-SC/UPC 10MTS',1,0.00,1,'2026-01-22 14:51:37'),(441,'C00065','CHAPA 3BOL',1,0.00,1,'2026-01-22 14:51:37'),(442,'C00066','CAJA PANDUIT 12 SALIDAS BLANCO',1,0.00,1,'2026-01-22 14:51:37'),(443,'C00067','BRAZO RF FIJO Y REGULABLE 30 CM',5,0.00,1,'2026-01-22 14:51:37'),(444,'C00068','BRAZO PARA ANTENA RF PIVOTANTE TIPO C 30 CM',5,0.00,1,'2026-01-22 14:51:37'),(445,'C00069','PATCH CORD DUPLEX FO OUTDOOR LC/UPC-LC/UPC 5MTS',1,0.00,1,'2026-01-22 14:51:37'),(446,'C00070','PATCH CORD DUPLEX FO OUTDOOR LC/UPC-SC/UPC 5MTS',1,0.00,1,'2026-01-22 14:51:37'),(447,'C00071','CONECTOR MINI DIN 4.3 P/CABLE 1/2 SUPERFLEX',1,0.00,1,'2026-01-22 14:51:37'),(448,'C00072','CONECTOR DIN 7/16 P/CABLE 1/2 SUPERFLEX',1,0.00,1,'2026-01-22 14:51:37'),(449,'C00073','CABLE DE ACERO TIPO RETENIDA 7/16',2,0.00,1,'2026-01-22 14:51:37'),(450,'C00074','BRAZOS EXTENSOR DE 1 MT CP 1/2',1,0.00,1,'2026-01-22 14:51:37'),(451,'C00075','SOPORTE RF 3 MTS CON 2 BRAZOS REGULABLES DE 20 A 40 CM Y ABRAZADERAS CON DIAMETRO 2\" A 4\"',5,0.00,1,'2026-01-22 14:51:37'),(452,'C00076','TABLERO DE BARRAS BORNERAS 63 x 22 x 20 cm',1,0.00,1,'2026-01-22 14:51:37'),(453,'C00077','CAJA DE DERIVACION 380/220V 6 SALIDAS',1,0.00,1,'2026-01-22 14:51:37'),(454,'C00078','DOBLE PLATAFORMA 1000x1200 C/ REF. Y BARANDA h=800 P/GABINETES EN POSTE',5,0.00,1,'2026-01-22 14:51:37'),(455,'C00079','MASTIL DE TUBO 4\" X 3m + BRAZOS',5,0.00,1,'2026-01-22 14:51:37'),(456,'C00080','ROSETA DE P/SOPORTE RF EN MONOPOSTE',5,0.00,1,'2026-01-22 14:51:37'),(457,'C00081','SOPORTE RF EN ROSETA (1 TUBO 2\" X 3.5m, 2 BRAZOS DE 49.5cm)',5,0.00,1,'2026-01-22 14:51:37'),(458,'C00082','CABLE 16MM P/BATERIA COLOR ROJO',1,0.00,1,'2026-01-22 14:51:37'),(459,'C00083','CABLE 16MM P/BATERIA COLOR NEGRO',1,0.00,1,'2026-01-22 14:51:37'),(460,'C00084','POSTE CAC 15/1200/225/450',1,0.00,1,'2026-01-22 14:51:37'),(461,'C00090','MOTOR',1,0.00,1,'2026-01-22 14:51:37'),(462,'C00091','ADAPTACION DE MONTANTES DE 2 1/2\" A 3\"',1,0.00,1,'2026-01-22 14:51:37'),(463,'C00092','TABLERO PDP',1,0.00,1,'2026-01-22 14:51:37'),(464,'C00093','CUC SUAVE 7A 35 mm²',2,0.00,1,'2026-01-22 14:51:37'),(465,'C00094','CUC DURO 7A 35 mm²',2,0.00,1,'2026-01-22 14:51:37'),(466,'C00095','TTRF-70 (60227 IEC53) 2x2,5 mm² 500V',2,0.00,1,'2026-01-22 14:51:37'),(467,'C00096','CAAI-S 3X50+2X25 mm² 0,6/1 KV',2,0.00,1,'2026-01-22 14:51:37'),(468,'C00097','CAAI-S 3x70+2x35 mm² 0,6/1 Kv',2,0.00,1,'2026-01-22 14:51:37'),(469,'C00098','N2XOH CAB. 3-1x50 mm² 0,6/1KV rojo,blanco,negro',2,0.00,1,'2026-01-22 14:51:37'),(470,'C00099','N2XOH CAB. 3-1x70 mm² 0,6/1KV rojo,blanco,negro',2,0.00,1,'2026-01-22 14:51:37'),(471,'C00100','N2XOH CAB. 3-1x120 mm² 0,6/1KV rojo,blanco,negro',2,0.00,1,'2026-01-22 14:51:37'),(472,'C00101','N2XOH CAB. 1x70 mm² 0,6/1KV blanco',2,0.00,1,'2026-01-22 14:51:37'),(473,'C00102','N2XOH CAB. 1x35 mm² 0,6/1KV blanco',2,0.00,1,'2026-01-22 14:51:37'),(474,'C00103','POSTE CAC 9/300/150/285',1,0.00,1,'2026-01-22 14:51:37'),(475,'C00104','POSTE CAC 11/350/165/330',1,0.00,1,'2026-01-22 14:51:37'),(476,'C00105','CABLE 16MM P/BATERIA COLOR AZUL',2,0.00,1,'2026-01-22 14:51:37'),(477,'C00106','PATCH CORD F.O. OUTDOOR LC/UPC-SC/UPC 20MTS',1,0.00,1,'2026-01-22 14:51:37'),(478,'C00107','PATCH CORD F.O. INDOOR LC/UPC-SC/UPC 20MTS',1,0.00,1,'2026-01-22 14:51:37'),(479,'C00108','CINTILLOS CV 150 NEGRO',6,0.00,1,'2026-01-22 14:51:37'),(480,'C00109','CINTILLOS CV 200 NEGRO',6,0.00,1,'2026-01-22 14:51:37'),(481,'C00110','CINTILLOS CV 250 NEGRO',6,0.00,1,'2026-01-22 14:51:37'),(482,'C00111','CINTILLOS CV 100 BLANCO',6,0.00,1,'2026-01-22 14:51:37'),(483,'C00112','PATCH CORD F.O. INDOOR LC/UPC-FC/UPC 10MTS',1,0.00,1,'2026-01-22 14:51:37'),(484,'C00113','PATCH CORD F.O. INDOOR SC/UPC-FC/UPC 10MTS',1,0.00,1,'2026-01-22 14:51:37'),(485,'C00114','CABLE 25MM P/BATERIA COLOR AZUL',2,0.00,1,'2026-01-22 14:51:37'),(486,'C00115','CABLE 25MM P/BATERIA COLOR NEGRO',2,0.00,1,'2026-01-22 14:51:37'),(487,'C00116','SOPORTE RF 3M X 2\"',5,0.00,1,'2026-01-22 14:51:37'),(488,'C00117','ETIQUETAS',5,0.00,1,'2026-01-22 14:51:37'),(489,'C00118','INTERR.TERMO.REG.3X252-630A(690V) 70/36KA 230/440V',1,0.00,1,'2026-01-22 14:51:37'),(490,'C00119','TRANSFORMADOR DE DISTRIBUCION TRIFASICO DE 250 KVA EXTERIOR',1,0.00,1,'2026-01-22 14:51:37'),(491,'C00120','POSTE CAC 9/300/2/140/275',1,0.00,1,'2026-01-22 14:51:37'),(492,'C00121','MANTO ASFALTICO (SBS) GRAVILLADA IMPERPOL 3000 NEGRO 1 X 10M E=3MM',1,0.00,1,'2026-01-22 14:51:37'),(493,'C00122','PEGAMENTO ULTRAPRIMER ASFALTICO 20 KG',9,0.00,1,'2026-01-22 14:51:37'),(494,'C00123','N2XOH CAB. 3-1x35 mm² 0,6/1KV rojo,negro,azul',2,0.00,1,'2026-01-22 14:51:37'),(495,'C00124','N2XOH CAB. 3-1x50 mm² 0,6/1KV rojo,negro,azul',2,0.00,1,'2026-01-22 14:51:37'),(496,'C00125','N2XOH CAB. 1x25 mm² 0,6/1KV blanco',2,0.00,1,'2026-01-22 14:51:37'),(497,'C00126','N2XOH CAB. 3-1x240 mm² 0,6/1KV rojo,negro,azul',2,0.00,1,'2026-01-22 14:51:37'),(498,'C00127','CABLE DE ACERO PARRES FORRADO 1/4',2,0.00,1,'2026-01-22 14:51:37'),(499,'C00128','CABLE DE ACERO PARRES FORRADO 3/8',2,0.00,1,'2026-01-22 14:51:37'),(500,'C00129','BORNERA 10 PERNOS COOPER',1,0.00,1,'2026-01-22 14:51:37'),(501,'C00130','BORNERA 18 PERNOS COOPER',1,0.00,1,'2026-01-22 14:51:37'),(502,'C00131','BORNERA 32 PERNOS COOPER',1,0.00,1,'2026-01-22 14:51:37'),(503,'C00132','BROCA DE 3/8 METAL',1,0.00,1,'2026-01-22 14:51:37'),(504,'C00133','DISCO DE CORTE METAL 4 1/2\"',1,0.00,1,'2026-01-22 14:51:37'),(505,'C00134','MANGA TERMOCONTRAIBLE 25 MM',2,0.00,1,'2026-01-22 14:51:37'),(506,'C00135','MANGA TERMOCONTRAIBLE 35 MM',2,0.00,1,'2026-01-22 14:51:37'),(507,'C00136','MANGA TERMOCONTRAIBLE 50 MM',2,0.00,1,'2026-01-22 14:51:37'),(508,'C00137','TERMINAL DOBLE OJO 10 DE 25 MM',1,0.00,1,'2026-01-22 14:51:37'),(509,'C00138','TERMINAL DOBLE OJO 10 DE 35 MM',1,0.00,1,'2026-01-22 14:51:37'),(510,'C00139','TERMINAL DOBLE OJO 10 DE 50 MM',1,0.00,1,'2026-01-22 14:51:37'),(511,'C00140','TOMACORRIENTE DOBLE',1,0.00,1,'2026-01-22 14:51:37'),(512,'C00141','CAAI-S 3X35+2X16 mm² 0,6/1 KV',2,0.00,1,'2026-01-22 14:51:37'),(513,'C00142','BROCA DE 9/32 METAL',1,0.00,1,'2026-01-22 14:51:37'),(514,'C00143','SIKADUR 31',1,0.00,1,'2026-01-22 14:51:37'),(515,'C00144','CONDUCTOR COPPERWELD DESNUDO 50 MM',1,0.00,1,'2026-01-22 14:51:37'),(516,'C00145','CABLE DE ACERO TIPO RETENIDA 3/16',2,0.00,1,'2026-01-22 14:51:37'),(517,'C00146','BARRA DE ALUMINIO DE 8 PINES',1,0.00,1,'2026-01-22 14:51:37'),(518,'C00147','BARRA DE ALUMINIO DE 10 PINES',1,0.00,1,'2026-01-22 14:51:37'),(519,'C00148','BARRA DE ALUMINIO DE 18 PINES',1,0.00,1,'2026-01-22 14:51:37'),(520,'C00149','CONECTOR AL/AL 50MM 2 PERNOS',1,0.00,1,'2026-01-22 14:51:37'),(521,'C00150','CONECTOR AB 3/4',1,0.00,1,'2026-01-22 14:51:37'),(522,'C03889','TABLERO DE DISTRIBUCIÓN COMPLETA PARA S.E. TRIFASICA, 250KVA MEDIDA APROX. 1000MM X 9000MM X 250MM',1,0.00,1,'2026-01-22 14:51:37'),(523,'C03890','SOPORTE REFORZADO TIPO PLATAFORMA DE DESCANSO CON 02 ARRIOSTRE 03 ABRAZADERAS CON CANAL C',5,0.00,1,'2026-01-22 14:51:37'),(524,'C03891','PAR DE ROSETA REFORZADA SEGÚN DISEÑO',5,0.00,1,'2026-01-22 14:51:37'),(525,'C03892','SOPORTES 2M X 2.5\" CON BRAZOS DE 49.5CM EN ROCETA PARA ANTENA RF EN POSTE DE 15M DIAMETRO = 23 CM',5,0.00,1,'2026-01-22 14:51:37'),(526,'C03893','SOPORTES 2M X 2.5\" CON BRAZOS DE 50CM EN ROCETA PARA RRUs EN POSTE DE 15M DIAMETRO = 22 CM',5,0.00,1,'2026-01-22 14:51:37'),(527,'C03894','ESCALERA VERTICAL 02 PARTES GALVANIZADO DE 7.20M X TUBO CUADRADO 3”X 3” X 4.5MM CON LÍNEA DE VIDA 3/8 ACERADO PARA POSTE DE 15M',5,0.00,1,'2026-01-22 14:51:37'),(528,'C03895','TABLERO DE DISTRIBUCIÓN COMPLETA PARA S.E. TRIFASICA DE 50 KVA MEDIDA APROX. 1000MM X 900MM X 250MM',1,0.00,1,'2026-01-22 14:51:37'),(529,'C03896','TABLERO DE DISTRIBUCIÓN COMPLETA PARA S.E. TRIFASICA DE 100 KVA MEDIDA APROX. 950 X 1000 X 250',1,0.00,1,'2026-01-22 14:51:37'),(530,'C03897','TABLERO DE DISTRIBUCIÓN COMPLETA PARA S.E. TRIFASICA DE 160 KVA MEDIDA APROX. 950 X 1000 X 250',1,0.00,1,'2026-01-22 14:51:37'),(531,'C03898','TTRF-70 (60227 IEC53) 2x2,5 mm² 500V gris oscuro',2,0.00,1,'2026-01-22 14:51:37'),(532,'C03899','CABLE CAAI AUTOPORTANTE 2X25 + NA 25 MM2',2,0.00,1,'2026-01-22 14:51:37'),(533,'C03900','TRANSFORMADOR DE DISTRIBUCION TRIFASICO DE 50 KVA EXTERIOR',1,0.00,1,'2026-01-22 14:51:37'),(534,'C03901','TRANSFORMADOR DE DISTRIBUCION TRIFASICO DE 100 KVA EXTERIOR',1,0.00,1,'2026-01-22 14:51:37'),(535,'C03902','TRANSFORMADOR DE DISTRIBUCION TRIFASICO DE 160 KVA EXTERIOR',1,0.00,1,'2026-01-22 14:51:37'),(536,'C03903','TRANSFORMADOR DE DISTRIBUCION TRIFASICO SUMERGIDO EN ACEITE DE 160 KVA(BUSHING FRONTAL)',1,0.00,1,'2026-01-22 14:51:37'),(537,'C03904','POSTE CAC 9/250/2/140/275',1,0.00,1,'2026-01-22 14:51:37'),(538,'C03905','POSTE CAC 9/300/140/280',1,0.00,1,'2026-01-22 14:51:37'),(539,'C03906','N2XOH CAB. 1x120 mm² 0,6/1KV blanco',2,0.00,1,'2026-01-22 14:51:37'),(540,'C03907','CAI CAB. 3x10+N10 mm² 0,6/1 KV',2,0.00,1,'2026-01-22 14:51:37'),(541,'C03908','N2XOH FLEX. 3-1x120 mm² 0,6/1 KV rojo,blanco,negro',2,0.00,1,'2026-01-22 14:51:37'),(542,'C03909','N2XOH CAB. 3-1x240 mm² 0,6/1KV rojo,blanco,negro',2,0.00,1,'2026-01-22 14:51:37'),(543,'C03910','BREAKER UNIPOLAR 80 AMP',1,0.00,1,'2026-01-22 14:51:37'),(544,'C03911','CABLE 25MM P/BATERIA COLOR ROJO',2,0.00,1,'2026-01-22 14:51:37'),(545,'C03912','SUMINISTRO DE POSTE DE 12 MTS',1,0.00,1,'2026-01-22 14:51:37'),(546,'C03913','BROCHA 2 1/2\" TUMI',1,0.00,1,'2026-01-22 14:51:37'),(547,'C03914','DISCO PULIFAN DE 4 1/2 X 40',1,0.00,1,'2026-01-22 14:51:37'),(548,'C03915','CINTA EMBALAJE',1,0.00,1,'2026-01-22 14:51:37'),(549,'C03916','CINTA FILM DELGADO',1,0.00,1,'2026-01-22 14:51:37'),(550,'C03917','DILUYENTE P33',10,0.00,1,'2026-01-22 14:51:37'),(551,'C03918','DISCO DE DESBASTE 4 1/2\"',1,0.00,1,'2026-01-22 14:51:37'),(552,'C03921','ESCOBILLA PARA FIERRO 64 PINCELES',1,0.00,1,'2026-01-22 14:51:37'),(553,'C03922','ESPATULA 3\"',1,0.00,1,'2026-01-22 14:51:37'),(554,'C03923','ESPUMA EXPANSIVA',1,0.00,1,'2026-01-22 14:51:37'),(555,'C03924','GALVANIZADO EN FRIO',1,0.00,1,'2026-01-22 14:51:37'),(556,'C03925','HOJA DE SIERRA SANFLEX',1,0.00,1,'2026-01-22 14:51:37'),(557,'C03926','KIT ANTIDERRAME',11,0.00,1,'2026-01-22 14:51:37'),(558,'C03927','KIT IMPOLAS',11,0.00,1,'2026-01-22 14:51:37'),(559,'C03928','KIT MACROPOXY BLANCO',11,0.00,1,'2026-01-22 14:51:37'),(560,'C03929','KIT MACROPOXY ROJO',11,0.00,1,'2026-01-22 14:51:37'),(561,'C03930','KIT SUMATANE BLANCO',11,0.00,1,'2026-01-22 14:51:37'),(562,'C03931','KIT SUMATANE ROJO',11,0.00,1,'2026-01-22 14:51:37'),(563,'C03932','LIJA FIERRO N°40',12,0.00,1,'2026-01-22 14:51:37'),(564,'C03933','PERNO HEXAGONAL G-5 UNC GALV. CAL. 3/8\" 1.1/2\"',1,0.00,1,'2026-01-22 14:51:37'),(565,'C03934','PERNO HEXAGONAL G-5 UNC GALV. CAL. R/CORR 1/2\"',1,0.00,1,'2026-01-22 14:51:37'),(566,'C03935','PERNO HEXAGONAL G-5 UNC GALV. CAL. R/CORR 3/4\" 2 1/2\"',1,0.00,1,'2026-01-22 14:51:37'),(567,'C03936','PERNO HEXAGONAL G-5 GALV. A325 5/8\" x 2 1/2\"',1,0.00,1,'2026-01-22 14:51:37'),(568,'C03937','PERTIGA TELESCOPICA TRIANGULAR 1.58MT - 6.44 MT 5 CUERPOS',1,0.00,1,'2026-01-22 14:51:37'),(569,'C03938','PLASTICO A3V1',2,0.00,1,'2026-01-22 14:51:37'),(570,'C03940','SIERRA COPAS MARCA BAHCO',1,0.00,1,'2026-01-22 14:51:37'),(571,'C03941','THINER ACRILICO',10,0.00,1,'2026-01-22 14:51:37'),(572,'C03942','TRAPO INDUSTRIAL',13,0.00,1,'2026-01-22 14:51:37'),(573,'C03943','TUERCA HEXAGONAL G-5 GALV. CAL. 3/4\"',1,0.00,1,'2026-01-22 14:51:37'),(574,'C03944','TUERCA HEXAGONAL G-5 GALV. CAL. 3/8\"',1,0.00,1,'2026-01-22 14:51:37'),(575,'C03945','TUERCA HEXAGONAL G-5 GALV. CAL. 5/8\"',1,0.00,1,'2026-01-22 14:51:37'),(576,'C03946','VARILLAS ROSCADA SAE 1020 GALV. CAL. 3/8\" 1.00 MTRS',1,0.00,1,'2026-01-22 14:51:37'),(577,'C03947','IPONLAC',10,0.00,1,'2026-01-22 14:51:37'),(578,'C03948','MACROPOXY 64(A+B) BLANCO',10,0.00,1,'2026-01-22 14:51:37'),(579,'C03949','MACROPOXY 64(A+B) ROJO BERMELLON',10,0.00,1,'2026-01-22 14:51:37'),(580,'C03950','SUMATANE HS BRILLANTE (A+B) BLANCO',10,0.00,1,'2026-01-22 14:51:37'),(581,'C03951','SUMATANE HS BRILLANTE (A+B) ROJO BERMELLON',10,0.00,1,'2026-01-22 14:51:37'),(582,'C03952','SOPORTE RF 2M X 2.5\" CON BRAZOS TUBO A TUBO DE 60CM',5,0.00,1,'2026-01-22 14:51:37'),(583,'C03953','PAR DE BRAZO RF DE 70CM DE TUBO A TUBO DE 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(584,'C03954','ROPA IGNIFUGA',1,0.00,1,'2026-01-22 14:51:37'),(585,'C03955','EPP PARA PERSONAL LDS',1,0.00,1,'2026-01-22 14:51:37'),(586,'C03956','ARNES CONFORTABLE ANTICAÍDAS, DE SUJECIÓN Y DE SUSPENSIÓN',1,0.00,1,'2026-01-22 14:51:37'),(587,'C03957','MOSQUETÓN OVAL LIGERO, SEGURO AUTOMÁTICO DE 3 TIEMPOS',1,0.00,1,'2026-01-22 14:51:37'),(588,'C03958','RESCUE POLEA ALTO RENDIMIENTO 36KN',1,0.00,1,'2026-01-22 14:51:37'),(589,'C03959','ANTICAÍDAS DESLIZANTE PARA CUERDA CON FUNCIÓN DE BLOQUEO',1,0.00,1,'2026-01-22 14:51:37'),(590,'C03960','DESCENSOR AUTOFRENANTE',1,0.00,1,'2026-01-22 14:51:37'),(591,'C03961','DESCENSOR EN OCHO',1,0.00,1,'2026-01-22 14:51:37'),(592,'C03962','ABSORBEDOR DE ENERGÍA ASAP’SORBER',1,0.00,1,'2026-01-22 14:51:37'),(593,'C03963','CONECTOR DIRECCIONAL DE GRAN ABERTURA Y DE BLOQUEO AUTOMÁTICO',1,0.00,1,'2026-01-22 14:51:37'),(594,'C03964','ESLINGA DE RESTRICCIÓN GANCHO PEQUEÑO',1,0.00,1,'2026-01-22 14:51:37'),(595,'C03965','ARNES CONTRACTOR 4 ARGOLLAS EN H',1,0.00,1,'2026-01-22 14:51:37'),(596,'C03966','ESLINGA DOBLE PIERNA CON GANCHO GRANDE CON ABSORBEDOR',1,0.00,1,'2026-01-22 14:51:37'),(597,'C03967','PERNO HEXAGONAL G-5 GALV. A325 1/2\" x 2\"',1,0.00,1,'2026-01-22 14:51:37'),(598,'C03968','TUERCA HEXAGONAL G-5 GALV. 1/2\"',1,0.00,1,'2026-01-22 14:51:37'),(599,'C03969','CAMISA IGNIFUGA ANTI-ARCO ATPV:27.7CAL/CM2 T/M AZUL MARINO',1,0.00,1,'2026-01-22 14:51:37'),(600,'C03970','PANTALON IGNIFUGO ANTI-ARCO ATPV:27.7CAL/CM2 T/30 AZUL MARINO',1,0.00,1,'2026-01-22 14:51:37'),(601,'C03971','CAMISA IGNIFUGA ANTI-ARCO ATPV:27.7CAL/CM2 T/L AZUL MARINO',1,0.00,1,'2026-01-22 14:51:37'),(602,'C03972','PANTALON IGNIFUGO ANTI-ARCO ATPV:27.7CAL/CM2 T/34 AZUL MARINO',1,0.00,1,'2026-01-22 14:51:37'),(603,'C03973','TAPA NUCA IGNIFUGO ANTI-ARCO IFR ATPR: 27.7 CAL/CM2',1,0.00,1,'2026-01-22 14:51:37'),(604,'C03974','BARBIQUEJO DE PROTECCION CONTRA ARCO ELECTRICO DE 32 CAL/CM2',1,0.00,1,'2026-01-22 14:51:37'),(605,'C03975','CORREA DE TELA IGNIFUGA CON AJUSTE DE HEBILLA',1,0.00,1,'2026-01-22 14:51:37'),(606,'C03976','CASCO TRIDENTE AZUL',1,0.00,1,'2026-01-22 14:51:37'),(607,'C03977','CASCO TRIDENTE ROJO',1,0.00,1,'2026-01-22 14:51:37'),(608,'C03978','RESPIRADOR DOBLE FILTRO',1,0.00,1,'2026-01-22 14:51:37'),(609,'C03979','ZAPATO PUNTA DE ACERO T-39',1,0.00,1,'2026-01-22 14:51:37'),(610,'C03980','GUANTES DE NYLON',1,0.00,1,'2026-01-22 14:51:37'),(611,'C03981','LENTES AF',1,0.00,1,'2026-01-22 14:51:37'),(612,'C03982','TRAJE TYVEK',1,0.00,1,'2026-01-22 14:51:37'),(613,'C03983','COLLARIN CERVICAL',1,0.00,1,'2026-01-22 14:51:37'),(614,'C03984','ESCALERA VERTICAL 02 PARTES GALVANIZADO DE 6.60M COM TUBO CUADRADO 2.5”X 2.5” X 3MM CON LÍNEA DE VIDA 3/8 ACERADO PARA POSTE 12M',5,0.00,1,'2026-01-22 14:51:37'),(615,'C03985','SOPORTE RF 1.20M X 2.5\" CON BRAZOS DE 30CM PARA POSTE CON DIÁMETRO DE 18CM',5,0.00,1,'2026-01-22 14:51:37'),(616,'C03986','SOPORTE RF 1.5M X 1.0M BACK TO BACK PARA LUMINARIAS CON DIÁMETRO 26.5CM',5,0.00,1,'2026-01-22 14:51:37'),(617,'C03987','SOPORTE RF CON 3 ABRAZADERAS DE 0.18 DIAMETROS PARA POSTE 12M',5,0.00,1,'2026-01-22 14:51:37'),(618,'C03988','POSTE CAC 15/400/2/180/405',1,0.00,1,'2026-01-22 14:51:37'),(619,'C03989','CASCO TRIDENTE BLANCO',1,0.00,1,'2026-01-22 14:51:37'),(620,'C03990','ZAPATO PUNTA DE ACERO T-42',1,0.00,1,'2026-01-22 14:51:37'),(621,'C03991','DISPENSADOR DE AGUA COLOR NEGRO',1,0.00,1,'2026-01-22 14:51:37'),(622,'C03992','PATCH CORD SIMPLEX FO OUTDOOR LC/UPC-SC/UPC 10MTS',1,0.00,1,'2026-01-22 14:51:37'),(623,'C03993','SOPORTE RF 2M X 2.5\" CON BRAZOS TUBO A TUBO DE 70CM',5,0.00,1,'2026-01-22 14:51:37'),(624,'C03994','ABRAZADERA T/OREJA DE 1°',1,0.00,1,'2026-01-22 14:51:37'),(625,'C03995','TUBO CORRUGADO FLEXIBLE 1°',1,4.20,1,'2026-01-22 14:51:37'),(626,'C03996','BANDEJA METALICA DE 1 RU 19° X 23 CM',1,0.00,1,'2026-01-22 14:51:37'),(627,'C03997','CABLE UTP CAT6 TX6000',8,0.00,1,'2026-01-22 14:51:37'),(628,'C03998','CAJA DE PARED SUPERFICIAL COLOR BLANCO',1,0.00,1,'2026-01-22 14:51:37'),(629,'C03999','CANALETA PVC BLANCA 24 X 14',1,0.00,1,'2026-01-22 14:51:37'),(630,'C04000','CINTA VELCRO 20 MM X 5 MTS COLOR NEGRO',1,0.00,1,'2026-01-22 14:51:37'),(631,'C04001','JACK CAT6 RJ 45 TG BLANCO HUESO',1,0.00,1,'2026-01-22 14:51:37'),(632,'C04002','NETHEY CAT6 24 AWG UTP PATCH CORD 10 FT BLUE',1,0.00,1,'2026-01-22 14:51:37'),(633,'C04003','PANNET FACE PLATE LINEA EJECUTIVA DE 1 PUERTO BLANCO OPACO',1,0.00,1,'2026-01-22 14:51:37'),(634,'C04004','BREAKER BIPOLAR 63 AMP',1,0.00,1,'2026-01-22 14:51:37'),(635,'C04005','PAR DE BRAZOS RF DE 40CM CON ABRAZADERAS T/SHAKIRA',1,0.00,1,'2026-01-22 14:51:37'),(636,'C04006','POSTE DE RESINA POLIESTER REFORZADO CON FIBRA DE VIDRIO 9MTS/500KG',1,0.00,1,'2026-01-22 14:51:37'),(637,'C04007','CANDADO MULTILOCK',1,0.00,1,'2026-01-22 14:51:37'),(638,'C04008','SOPORTE DE 1.5 X 2” CON BRAZOS DE 30CM A ROSETA P/MONOPOLO DIÁMETRO 21CM',5,0.00,1,'2026-01-22 14:51:37'),(639,'C04009','ROSETA DE 3/8 P/MONOPOLO DIÁMETRO 21CM',5,0.00,1,'2026-01-22 14:51:37'),(640,'C04010','SPRAY NEGRO',1,0.00,1,'2026-01-22 14:51:37'),(641,'C04011','MONTANTE 2 1/2\" X 5/16\" X 5000MM',1,0.00,1,'2026-01-22 14:51:37'),(642,'C04012','MONTANTE 2 1/2\" X 5/16\" X 3000MM',1,0.00,1,'2026-01-22 14:51:37'),(643,'C04013','MONTANTE 2 1/2\" X 5/16\" X 500MM',1,0.00,1,'2026-01-22 14:51:37'),(644,'C04014','PLANCHA METALICA 2 1/2\" X 5/16\" X 500MM',1,0.00,1,'2026-01-22 14:51:37'),(645,'C04015','CLIPS DE ACERO 2 1/2\" x 1/4\" X 127MM',1,0.00,1,'2026-01-22 14:51:37'),(646,'C04016','MÁSTIL DE 2.40M X 4” + 1 SOPORTE RF 3M X 2” CON BRAZO P/ MONOPOLO DE DIAMETRO 40CM',5,0.00,1,'2026-01-22 14:51:37'),(647,'C04017','SOPORTE RF 3M X 2° CON BRAZOS DE 50CM P/MONOPOLO DE DIAMETRO 49CM',5,0.00,1,'2026-01-22 14:51:37'),(648,'C04018','SOPORTE RF 2M X 2° CON BRAZOS DE 30CM TUBO A TUBO DE 2.5°',5,0.00,1,'2026-01-22 14:51:37'),(649,'C04019','BRAZOS EXTENSOR DE 40 CM CP 1/2',1,0.00,1,'2026-01-22 14:51:37'),(650,'C04020','CHAPA CRUCE AMERICANO',1,0.00,1,'2026-01-22 14:51:37'),(651,'C04021','CHAPA MEDIO CRUCE',1,0.00,1,'2026-01-22 14:51:37'),(652,'C04022','CIERRE DE EMPALME VERTICAL 48 HILOS',1,0.00,1,'2026-01-22 14:51:37'),(653,'C04023','ESCALERA VERTICAL 02 PARTES GALVANIZADO DE 6.40M CON TUBO CUADRADO 2.5°X 2.5°X 3MM CON LÍNEA DE VIDA 3/8 ACERADO PARA POSTE 12M',5,0.00,1,'2026-01-22 14:51:37'),(654,'C04024','SOPORTE P/LUMINARIA 1.5M X 1.1/2\" X 3MM',5,0.00,1,'2026-01-22 14:51:37'),(655,'C04025','MÁSTIL DE 1.5M X 3° P/ANTENA RF TIPO ABRAZADERA 0.40 CM EN POSTE 9M CON DIÁMETRO 15CM',5,0.00,1,'2026-01-22 14:51:37'),(656,'C04026','SOPORTE DE 1.5M X 1M PARA 1 LUMINARIA EN POSTE CON DIÁMETRO 17CM',5,0.00,1,'2026-01-22 14:51:37'),(657,'C04027','SOPORTE RF 1.5M X 2° CON BRAZOS TUBO A TUBO DE 70CM',5,0.00,1,'2026-01-22 14:51:37'),(658,'C04028','SOPORTE RF 2M X 2” CON BRAZOS DE 30CM A ROSETA P/MONOPOLO DIÁMETRO 21CM',5,0.00,1,'2026-01-22 14:51:37'),(659,'C04029','POSTE CAC 15/350/2/225/450',1,0.00,1,'2026-01-22 14:51:37'),(660,'C04030','SOPORTE RF 1 M X 2.5° CON BRAZOS DE 30CM POSTE CON DIÁMETRO DE 24CM',1,0.00,1,'2026-01-22 14:51:37'),(661,'C04031','ALUMBADO PUBLICO 50W IP66 + FOTOCELDA',1,0.00,1,'2026-01-22 14:51:37'),(662,'C04032','POSTE CAC 11/400/150/315',1,0.00,1,'2026-01-22 14:51:37'),(663,'C04033','POSTE CAC 15/600/210/435',1,0.00,1,'2026-01-22 14:51:37'),(664,'C04034','POSTE CAC 18/900/300/570',1,0.00,1,'2026-01-22 14:51:37'),(665,'C04035','ESCALERA VERTICAL GALVANIZADO DE 3.40M CON TUBO CUADRADO 3°X 3° X 3MM CON LÍNEA DE VIDA 3/8 ACERADO PARA POSTE DE 11M',5,0.00,1,'2026-01-22 14:51:37'),(666,'C04036','ABRAZADERA PARA LUMINARIA PARA POSTE CON DIÁMETRO 18.9CM',5,0.00,1,'2026-01-22 14:51:37'),(667,'C04037','ABRAZADERA PARA LUMINARIA PARA POSTE CON DIÁMETRO 29.4CM',5,0.00,1,'2026-01-22 14:51:37'),(668,'C04038','MASTIL RF DE 1.5M X 3° PARA ANTENA EASY MACRO CON BASE TIPO ABRAZADERA 0.40 CM',5,0.00,1,'2026-01-22 14:51:37'),(669,'C04039','POSTE CAC 18/400/240/510',1,0.00,1,'2026-01-22 14:51:37'),(670,'C04040','CAJA DE PVC 30X30',1,0.00,1,'2026-01-22 14:51:37'),(671,'C04041','SOPORTE RF 2M X 2° CON BRAZOS DE 70CM DE TUBO A TUBO DE 2° A 1.5°',5,0.00,1,'2026-01-22 14:51:37'),(672,'C04042','SOPORTE RF 3M X 2° CON BRAZOS DE 70CM DE TUBO A TUBO DE 2° A 1.5°',5,0.00,1,'2026-01-22 14:51:37'),(673,'C04043','PAR DE BRAZOS RF 40CM DE TUBO A TUBO DE 2° A 2.5°',5,0.00,1,'2026-01-22 14:51:37'),(674,'C04044','SOPORTE DE 1.5M X 1M PARA 1 LUMINARIA EN POSTE CON DIÁMETRO 21CM',5,0.00,1,'2026-01-22 14:51:37'),(675,'C04045','SOPORTE RF 1 M X 2° CON BRAZOS DE 30CM',5,0.00,1,'2026-01-22 14:51:37'),(676,'C04046','SOPORTE RF 1.5M X 2° CON BRAZOS TUBO A TUBO DE 60CM',5,0.00,1,'2026-01-22 14:51:37'),(677,'C04047','CUELLO DE GANZO PARA FO DE 1.5° X 3M X 1.8',5,0.00,1,'2026-01-22 14:51:37'),(678,'C04048','ESCALERA VERTICAL 2 PARTES GALVANIZADO DE 9.40M COM TUBO CUADRADO 2.5°X 2.5° X 3MM CON LÍNEA DE VIDA 3/8 ACERADO PARA POSTE 12M',5,0.00,1,'2026-01-22 14:51:37'),(679,'C04049','ESCALERA VERTICAL 2 PARTES GALVANIZADO DE 9.40M COM TUBO CUADRADO 2.5°X 2.5° X 3MM CON LÍNEA DE VIDA 3/8 ACERADO PARA POSTE 18M',5,0.00,1,'2026-01-22 14:51:37'),(680,'C04050','PAR DE ROSETA REFORZADA P/POSTE CON DIÁMETRO DE 25CM',5,0.00,1,'2026-01-22 14:51:37'),(681,'C04051','PAR DE ROSETA REFORZADA P/POSTE CON DIÁMETRO DE 26CM',5,0.00,1,'2026-01-22 14:51:37'),(682,'C04052','SOPORTE DE 1.5M X 1M PARA 1 LUMINARIA EN POSTE CON DIÁMETRO 28CM',5,0.00,1,'2026-01-22 14:51:37'),(683,'C04053','SOPORTE DE 1.5M X 1M PARA 1 LUMINARIA EN POSTE CON DIÁMETRO 37CM',5,0.00,1,'2026-01-22 14:51:37'),(684,'C04054','SOPORTE RF 1.40M X 2.5° CON BRAZOS DE 30CM PARA POSTE DE 26CM',5,0.00,1,'2026-01-22 14:51:37'),(685,'C04055','SOPORTE RF 1.40M X 2.5° CON BRAZOS DE 30CM PARA POSTE DE 28CM',5,0.00,1,'2026-01-22 14:51:37'),(686,'C04056','SOPORTE RF 1.40M X 2.5° CON BRAZOS DE 30CM PARA POSTE DE 29CM',5,0.00,1,'2026-01-22 14:51:37'),(687,'C04057','SOPORTE RF 1.7 M X 2° CON BRAZOS DE 40CM',5,0.00,1,'2026-01-22 14:51:37'),(688,'C04058','SOPORTE RF 1.7 M X 2.5° CON BRAZOS DE 40CM POSTE CON DIÁMETRO DE 25CM',5,0.00,1,'2026-01-22 14:51:37'),(689,'C04059','PAR DE ROSETA REFORZADA P/POSTE CON DIÁMETRO DE 30CM',5,0.00,1,'2026-01-22 14:51:37'),(690,'C04060','SOPORTE DE 1.5M X 1M PARA 1 LUMINARIA EN POSTE CON DIÁMETRO 43CM',5,0.00,1,'2026-01-22 14:51:37'),(691,'C04061','SOPORTE RF 1.7 M X 2.5° CON BRAZOS DE 40CM POSTE CON DIÁMETRO DE 30CM',5,0.00,1,'2026-01-22 14:51:37'),(692,'C04062','SOPORTE RF 1M X 2.5° CON BRAZOS DE 40CM PARA POSTE DE 33CM',5,0.00,1,'2026-01-22 14:51:37'),(693,'C04063','ESCALERILLA GALVANIZADA DE 0.30M x 3 M',1,0.00,1,'2026-01-22 14:51:37'),(694,'C04064','ESCUADRA PLANA GALVANIZADA 5 x 5 CM PARA UNION DE ESCALERILLA VERTICAL Y HORIZONTAL',5,0.00,1,'2026-01-22 14:51:37'),(695,'C04065','FINAL DE ESCALERRILLA 0.30M',1,0.00,1,'2026-01-22 14:51:37'),(696,'C04066','JOTA GALVANIZADA DE 3/8 CON TUERCA Y CONTRATUERCA',1,0.00,1,'2026-01-22 14:51:37'),(697,'C04067','LS GALVANIZADA DE 10 x 5CM PARA PISO',5,0.00,1,'2026-01-22 14:51:37'),(698,'C04068','UNION EMPALME GALVANIZADA 5CM',5,0.00,1,'2026-01-22 14:51:37'),(699,'C04069','POSTE CAC 18/600/2/240/510',1,0.00,1,'2026-01-22 14:51:37'),(700,'C04070','SOPORTE RF 1.7 M X 2.5° CON BRAZOS DE 40CM POSTE CON DIÁMETRO DE 31CM',5,0.00,1,'2026-01-22 14:51:37'),(701,'C04071','SOPORTE RF 1M X 2.5° CON BRAZOS DE 30CM',5,0.00,1,'2026-01-22 14:51:37'),(702,'C04072','SOPORTE DE 1.5M X 1M PARA 1 LUMINARIA EN POSTE CON DIÁMETRO 42CM',5,0.00,1,'2026-01-22 14:51:37'),(703,'C04073','POSTE CAC 18/400/210/480',1,0.00,1,'2026-01-22 14:51:37'),(704,'C04074','POSTE CAC 15/400/310/480',1,0.00,1,'2026-01-22 14:51:37'),(705,'C04075','SOPORTE RF 1 M X 2.5° CON BRAZOS DE 40CM P/TORRE AUTOSOPORTADA',5,0.00,1,'2026-01-22 14:51:37'),(706,'C04076','SOPORTE RF 1 M X 2.5° CON BRAZOS DE 40CM P/TORRE VENTADA',5,0.00,1,'2026-01-22 14:51:37'),(707,'C04077','CHALECO ROJO CON LOGO TALLA L',1,0.00,1,'2026-01-22 14:51:37'),(708,'C04078','CHALECO ROJO CON LOGO TALLA M',1,0.00,1,'2026-01-22 14:51:37'),(709,'C04079','CHALECO ROJO CON LOGO TALLA XL',1,0.00,1,'2026-01-22 14:51:37'),(710,'C04080','CHALECO ROJO CON LOGO TALLA XXL',1,0.00,1,'2026-01-22 14:51:37'),(711,'C04081','PANTALON JEAN CON CINTA REFLECTIVA Y LOGO TALLA 28',1,0.00,1,'2026-01-22 14:51:37'),(712,'C04082','PANTALON JEAN CON CINTA REFLECTIVA Y LOGO TALLA 30',1,0.00,1,'2026-01-22 14:51:37'),(713,'C04083','PANTALON JEAN CON CINTA REFLECTIVA Y LOGO TALLA 32',1,0.00,1,'2026-01-22 14:51:37'),(714,'C04084','PANTALON JEAN CON CINTA REFLECTIVA Y LOGO TALLA 34',1,0.00,1,'2026-01-22 14:51:37'),(715,'C04085','PANTALON JEAN CON CINTA REFLECTIVA Y LOGO TALLA 40',1,0.00,1,'2026-01-22 14:51:37'),(716,'C04086','POLO CUELLO CAMISERO MANGA LARGA CON LOGO TALLA L',1,0.00,1,'2026-01-22 14:51:37'),(717,'C04087','POLO CUELLO CAMISERO MANGA LARGA CON LOGO TALLA M',1,0.00,1,'2026-01-22 14:51:37'),(718,'C04088','POLO CUELLO CAMISERO MANGA LARGA CON LOGO TALLA XL',1,0.00,1,'2026-01-22 14:51:37'),(719,'C04089','POLO CUELLO CAMISERO MANGA LARGA CON LOGO TALLA XXL',1,0.00,1,'2026-01-22 14:51:37'),(720,'C04090','BOTA DIELECTRICA WALKER TALLA 40',14,0.00,1,'2026-01-22 14:51:37'),(721,'C04091','BOTA DIELECTRICA WALKER TALLA 41',14,0.00,1,'2026-01-22 14:51:37'),(722,'C04092','BOTA DIELECTRICA WALKER TALLA 43',14,0.00,1,'2026-01-22 14:51:37'),(723,'C04093','BASE TIPO CHUPETE REFORZADO DE 40 X 27 X 26 CM CON BASE MÁSTIL DE 3 M X 4°',5,0.00,1,'2026-01-22 14:51:37'),(724,'C04094','SOPORTE RF 3M X 2.5\" CON BRAZOS DE 40CM A ROSETA',5,0.00,1,'2026-01-22 14:51:37'),(725,'C04095','PERILLA POSTE BT Y ADITIVO INHIBIDOR DE CORROSION',1,0.00,1,'2026-01-22 14:51:37'),(726,'C04096','PERILLA POSTE MT Y ADITIVO INHIBIDOR DE CORROSION',1,0.00,1,'2026-01-22 14:51:37'),(727,'C04097','POSTE CAC 11/250/165/330',1,0.00,1,'2026-01-22 14:51:37'),(728,'C04098','POSTE CAC 9/250/150/285',1,0.00,1,'2026-01-22 14:51:37'),(729,'C04099','CABLE UTP CAT 5E APANTALLADO COLOR NEGRO',2,0.00,1,'2026-01-22 14:51:37'),(730,'C04100','SOPORTE RF 1 M X 2.5° CON BRAZOS DE 30CM POSTE CON DIÁMETRO DE 34CM',5,0.00,1,'2026-01-22 14:51:37'),(731,'C04101','PAR DE ROSETA REFORZADA P/POSTE CON DIÁMETRO DE 31CM',5,0.00,1,'2026-01-22 14:51:37'),(732,'C04102','SOPORTE RF 1.7 M X 2.5° CON BRAZOS DE 40CM POSTE CON DIÁMETRO DE 22CM',5,0.00,1,'2026-01-22 14:51:37'),(733,'C04103','SOPORTE DE 1.5M X 1M PARA 1 LUMINARIA EN POSTE CON DIÁMETRO 32CM',5,0.00,1,'2026-01-22 14:51:37'),(734,'C04104','PAR DE ROSETA REFORZADA P/POSTE CON DIÁMETRO DE 22CM',5,0.00,1,'2026-01-22 14:51:37'),(735,'C04105','ESCALERA VERTICAL 2 PARTES GALVANIZADO DE 6.40M COM TUBO CUADRADO 2.5°X 2.5° X 3MM CON LÍNEA DE VIDA 3/8 ACERADO PARA POSTE 18M',5,0.00,1,'2026-01-22 14:51:37'),(736,'C04106','SOPORTE DE 1.5M X 1M PARA 1 LUMINARIA EN POSTE CON DIÁMETRO 30CM',5,0.00,1,'2026-01-22 14:51:37'),(737,'C04107','SOPORTE RF 1.5M X 2.5° CON BRAZOS DE 30CM PARA POSTE 9 M',5,0.00,1,'2026-01-22 14:51:37'),(738,'C04108','POSTE CAC 18/600/210/480',1,0.00,1,'2026-01-22 14:51:37'),(739,'C04109','ESCALERA VERTICAL 2 PARTES GALVANIZADO DE 9.40M COM TUBO CUADRADO 2.5°X 2.5° X 3MM CON LÍNEA DE VIDA 3/8 ACERADO PARA POSTE 15M',5,0.00,1,'2026-01-22 14:51:37'),(740,'C04110','SOPORTE DE 1.5M X 1M PARA 1 LUMINARIA EN POSTE CON DIÁMETRO 14CM',5,0.00,1,'2026-01-22 14:51:37'),(741,'C04111','SOPORTE DE 1.5M X 1M PARA 1 LUMINARIA EN POSTE CON DIÁMETRO 19CM',5,0.00,1,'2026-01-22 14:51:37'),(742,'C04112','SOPORTE DE 1.5M X 1M PARA 1 LUMINARIA EN POSTE CON DIÁMETRO 20CM',5,0.00,1,'2026-01-22 14:51:37'),(743,'C04113','SOPORTE DE 1.5M X 1M PARA 1 LUMINARIA EN POSTE CON DIÁMETRO 33CM',5,0.00,1,'2026-01-22 14:51:37'),(744,'C04114','SOPORTE DE 1.5M X 1M PARA 1 LUMINARIA EN POSTE CON DIÁMETRO 40CM',5,0.00,1,'2026-01-22 14:51:37'),(745,'C04115','SOPORTE RF 1 M X 2° CON BRAZOS DE 30CM POSTE CON DIÁMETRO DE 23CM',5,0.00,1,'2026-01-22 14:51:37'),(746,'C04116','SOPORTE RF 1 M X 2° CON BRAZOS DE 30CM POSTE CON DIÁMETRO DE 24CM',5,0.00,1,'2026-01-22 14:51:37'),(747,'C04117','SOPORTE RF 1 M X 2° CON BRAZOS DE 30CM POSTE CON DIÁMETRO DE 30CM',5,0.00,1,'2026-01-22 14:51:37'),(748,'C04118','SOPORTE RF 2M X 2° CON BRAZOS DE 60CM DE TUBO A ROSETA',5,0.00,1,'2026-01-22 14:51:37'),(749,'C04119','BREAKER TRIPOLAR 63 AMP',1,0.00,1,'2026-01-22 14:51:37'),(750,'C04120','CABLE UTP CAT6 COLOR PLOMO',2,0.00,1,'2026-01-22 14:51:37'),(751,'C04121','CAPUCHA RJ-45',1,0.00,1,'2026-01-22 14:51:37'),(752,'C04122','CONECTOR RJ-45 CAT6',1,0.00,1,'2026-01-22 14:51:37'),(753,'C04123','TERMINAL OJO 6',1,0.00,1,'2026-01-22 14:51:37'),(754,'C04124','UNION 10MM',1,0.00,1,'2026-01-22 14:51:37'),(755,'C04125','UNION RJ-45',1,0.00,1,'2026-01-22 14:51:37'),(756,'C04126','UNION TUBULAR 16MM',1,0.00,1,'2026-01-22 14:51:37'),(757,'C04127','VULCANIZANTE',1,0.00,1,'2026-01-22 14:51:37'),(758,'C04128','AMARRE PREFORMADO VERDE 5/8',1,0.00,1,'2026-01-22 14:51:37'),(759,'C04129','SOPORTE RF 2M X 2° CON BRAZOS DE 20CM CON PLANCHAS DE 20X20 CM P/PARED',5,0.00,1,'2026-01-22 14:51:37'),(760,'C04130','ACCESORIO DE REJILLA PARA AIRE ACONDICIONADO',5,0.00,1,'2026-01-22 14:51:37'),(761,'C04131','MARCO METALICO GALVANIZADO P/SHELTER DE 0.65M X 1.75M CON MALLA COCADA 2.5CM A 4CM + ACCESORIOS',5,0.00,1,'2026-01-22 14:51:37'),(762,'C04132','PUERTA METALICA GALVANIZADA + ACCESORIOS',5,0.00,1,'2026-01-22 14:51:37'),(763,'C04133','SOPORTE DE 1.5M X 2° CON BRAZOS DE 25CM 2 SECTORES P/POSTE CON DIÁMETRO 22CM',1,0.00,1,'2026-01-22 14:51:37'),(764,'C04134','SOPORTE DE 1.5M X 2° CON BRAZOS DE 25CM 2 SECTORES P/MASTIL DE 3.5°',1,0.00,1,'2026-01-22 14:51:37'),(765,'C04135','SOPORTE DE 1.5M X 2° CON BRAZOS DE 25CM 1 SECTOR P/POSTE CON DIAMETRO 22CM',1,0.00,1,'2026-01-22 14:51:37'),(766,'C04136','SOPORTE RF 1 M X 2° CON BRAZOS DE 25CM POSTE CON DIÁMETRO DE 22CM',1,0.00,1,'2026-01-22 14:51:37'),(767,'C04137','ADECUACIÓN TIPO ROSETA(BRIDA) P/MONOPOLO DE ESPESOR 1/4 CON DIÁMETRO DE 52CM',1,0.00,1,'2026-01-22 14:51:37'),(768,'C04138','SOPORTE RF 3M X 2° CON BRAZOS DE 30CM FIJO P/ROSETA',1,0.00,1,'2026-01-22 14:51:37'),(769,'C04139','SOPORTE DE 1.20M X 2° CON BRAZOS DE 25CM P/POSTE CON DIÁMETRO DE 23CM',1,0.00,1,'2026-01-22 14:51:37'),(770,'C04140','SOPORTE DE 1.20M X 2° CON BRAZOS DE 25CM P/MÁSTIL DE 3°',1,0.00,1,'2026-01-22 14:51:37'),(771,'C04141','ADECUACIÓN ESTRUCTURA 1 CUERPO DE 3M X 3/16 DIÁMETROS 33CM A 30CM CON BRIDAS BASE DIÁMETRO 53CM Y PARTE SUPERIOR DIÁMETRO 50CM',1,0.00,1,'2026-01-22 14:51:37'),(772,'C04142','PAR DE ROSETA REFORZADA P/POSTE CON DIÁMETRO DE 20CM',1,0.00,1,'2026-01-22 14:51:37'),(773,'C04143','SOPORTE BAT BLADE 1.4M X 2° CON BRAZOS DE 30CM',1,0.00,1,'2026-01-22 14:51:37'),(774,'C04144','SOPORTE RF 1.7 M X 2.5° CON BRAZOS DE 40CM POSTE CON DIÁMETRO DE 20CM',1,0.00,1,'2026-01-22 14:51:37'),(775,'C04145','BRAZOS DE SUJECION AMARRE ENTRE 3 SOPORTES LARGUEROS DE 1.305M',1,0.00,1,'2026-01-22 14:51:37'),(776,'C04146','SOPORTE DE 4.5M X 2.5° CON BRAZOS DE 50CM P/3 SECTORES EN POSTE CON DIÁMETRO DE 49.8CM',1,0.00,1,'2026-01-22 14:51:37'),(777,'C04147','SOPORTE DE 1M X 2° CON 02 BRAZOS REFORZADOS DE 90CM',1,0.00,1,'2026-01-22 14:51:37'),(778,'C04148','POSTE DE RESINA POLIESTER REFORZADO CON FIBRA DE VIDRIO 9MTS/250KG',1,0.00,1,'2026-01-22 14:51:37'),(779,'C04149','SOPORTE REFORZADO DE 5M X 2° CON BRAZOS DE 20CM P/TORRE VENTADA MONTANTE DE 1.5°',1,0.00,1,'2026-01-22 14:51:37'),(780,'C04150','POSTE CAC 18/600/245/515',1,0.00,1,'2026-01-22 14:51:37'),(781,'C04151','ENCHUFE INDUSTRIAL 15A',1,0.00,1,'2026-01-22 14:51:37'),(782,'C04152','ENCHUFE PLANO C13',1,0.00,1,'2026-01-22 14:51:37'),(783,'C04153','ADAPTADOR UNION RJ-45',1,0.00,1,'2026-01-22 14:51:37'),(784,'C04154','SOPORTE RF 2.5 M X 2° CON BRAZOS DE 40CM POSTE CON DIÁMETRO DE 30CM',1,0.00,1,'2026-01-22 14:51:37'),(785,'C04155','SOPORTE DE 1.4M X 2° CON BRAZOS DE 30CM PARA 1 BLADE EN POSTE CON DIAMETRO 24CM',1,0.00,1,'2026-01-22 14:51:37'),(786,'C04156','SOPORTE RF 1 M X 2.5° CON BRAZOS DE 30CM A ROSETA',1,0.00,1,'2026-01-22 14:51:37'),(787,'C04157','SOPORTE RF 1.5M X 2.5° CON BRAZOS DE 30CM A ROSETA',1,0.00,1,'2026-01-22 14:51:37'),(788,'C04158','PAR DE ROSETA REFORZADA P/POSTE CON DIÁMETRO DE 19CM',1,0.00,1,'2026-01-22 14:51:37'),(789,'C04159','PAR DE BRAZOS RF 30CM PARA 1 BLADE EN POSTE CON DIÁMETRO DE 21CM',1,0.00,1,'2026-01-22 14:51:37'),(790,'C04160','LUMINARIA SOLAR LED 100 WATS',1,0.00,1,'2026-01-22 14:51:37'),(791,'C04161','POSTE CAC 9/250/150/285 + PERILLA POSTE BT Y ADITIVO INHIBIDOR DE CORROSION',1,0.00,1,'2026-01-22 14:51:37'),(792,'C04162','SOPORTE ANTENA ADU 60MM X 3 MM X 1 M A POSTE',1,0.00,1,'2026-01-22 14:51:37'),(793,'C04163','SOPORTE BLADE 60MM X 3 MM X 1.2 M A POSTE',1,0.00,1,'2026-01-22 14:51:37'),(794,'C04164','ESCALERA VERTICAL GALVANIZADO DE 6M CON TUBO CUADRADO 2°X 2° CON LÍNEA DE VIDA',1,0.00,1,'2026-01-22 14:51:37'),(795,'C04165','ESCALERILLA GALVANIZADA DE 0.30M x 2.3 M',1,0.00,1,'2026-01-22 14:51:37'),(796,'C04166','PATCH CORD FO DUPLEX OUTDOOR LC/UPC-LC/UPC 20MTS',1,0.00,1,'2026-01-22 14:51:37'),(797,'C04167','BRAZO RF DE 60 CM TUBO A TUBO 2\"',5,0.00,1,'2026-01-22 14:51:37'),(798,'C04168','BRAZO RF REGULABLE DE 40 CM P/TORRE AUTOSOPORTADA TRIANGULAR CON MONTANTE DE 3\" Y AGARRE TUBO DE 2\"',5,0.00,1,'2026-01-22 14:51:37'),(799,'C04169','BRAZO RF DE 70 CM TUBO A TUBO 2\"',5,0.00,1,'2026-01-22 14:51:37'),(800,'C04170','BRAZO RF DE 40 CM TUBO A TUBO 2\"',5,0.00,1,'2026-01-22 14:51:37'),(801,'C04171','CABLE TIERRA 16MM AMARILLO/VERDE',2,0.00,1,'2026-01-22 14:51:37'),(802,'C04172','CABLE VULCANIZADO 2X8AWG APANTALLADO NEGRO',2,0.00,1,'2026-01-22 14:51:37'),(803,'C04173','PARANTE DE ESCALERA PEATONAL 3M CON TUBO CUADRADO 3°X 3°',5,0.00,1,'2026-01-22 14:51:37'),(804,'C04174','ABRAZADERA CON PUAS FIERRO NEGRO',5,0.00,1,'2026-01-22 14:51:37'),(805,'C04175','CASE GAMER HALION DRAGON CR15 500W 4X12 LEO USB 3.0',1,0.00,1,'2026-01-22 14:51:37'),(806,'C04176','MEMORIA RAM TEAMGROUP T-FORCE VULCAN Z DDR4 16GB 3200',1,0.00,1,'2026-01-22 14:51:37'),(807,'C04177','PLACA GIGABYTE H470M-H DDR4 LGA 1200',1,0.00,1,'2026-01-22 14:51:37'),(808,'C04178','PROCESADOR INTEL CORE 17 10700 2.9GHZ-16MB 1 LGA 1200 65W, 14 NM. CACHE 16MB, 8 NUCLEOS, 16 HILOS',1,0.00,1,'2026-01-22 14:51:37'),(809,'C04179','SSD SOLIDO 500GB KINGSTON NV2 M.2 2280 NVME PCIE GEN 4.0',1,0.00,1,'2026-01-22 14:51:37'),(810,'C04180','VGA MSI GE FORCE NVIDIA GT 1030 4GB GDDR4 LP',1,0.00,1,'2026-01-22 14:51:37'),(811,'C04181','LAPTOP DELL INSPIRON 15 CORE I5-1235U, RAM 8GB, SSD 512GB,PANTALLA 15.6\" FHD',1,0.00,1,'2026-01-22 14:51:37'),(812,'C04182','LAPTOP DELL INSPIRON 15 CORE I7-1255U, RAM 8GB, SSD 512GB,PANTALLA 15.6\" FHD, INTEL IRIS XE GRAPHICS',1,0.00,1,'2026-01-22 14:51:37'),(813,'C04183','CINTILLO METALICO 15CM',6,0.00,1,'2026-01-22 14:51:37'),(814,'C04184','ESCALERA VERTICAL GALVANIZADO DE 9M CON TUBO CUADRADO 2°X 2° CON LÍNEA DE VIDA',5,0.00,1,'2026-01-22 14:51:37'),(815,'C04185','SOPORTE RF 2 M X 2° CON BRAZO DE 30CM DE ABRAZADERA A TUBO',5,0.00,1,'2026-01-22 14:51:37'),(816,'C04186','SOPORTE RF 2 M X 2° CON BRAZO DE 30CM DE ROSETA A TUBO',5,0.00,1,'2026-01-22 14:51:37'),(817,'C04187','SOPORTE P/GABINETE MINIPACK',5,0.00,1,'2026-01-22 14:51:37'),(818,'C04188','SOPORTE RF 3 TUBOS 1 M X 2° + 6 BRAZOS DE ANGULO 2°X1/8° + 2 ROSETAS DE PL LISA 1/4°',5,0.00,1,'2026-01-22 14:51:37'),(819,'C04189','CINTILLO METALICO 10CM',6,0.00,1,'2026-01-22 14:51:37'),(820,'C04190','TABLERO MDP 3F-220VAC-60HZ ROOFTOP',1,0.00,1,'2026-01-22 14:51:37'),(821,'C04191','TABLERO PDP 3F-220VAC-60HZ ROOFTOP',1,0.00,1,'2026-01-22 14:51:37'),(822,'C04192','TABLERO PDP 2F-220VAC-60HZ TRIFASICO GREENFIELD',1,0.00,1,'2026-01-22 14:51:37'),(823,'C04193','SOPORTE OMNISECTORIAL',5,0.00,1,'2026-01-22 14:51:37'),(824,'C04194','MODIFICACIÓN DE BRAZOS DE SUJECION AMARRE ENTRE 3 SOPORTES LARGUEROS DE 1.305M',1,0.00,1,'2026-01-22 14:51:37'),(825,'C04195','MODIFICACIÓN DE PAR DE BRAZOS DE 50CM PARA 3 SECTORES EN POSTE CON DIÁMETRO DE 40CM',1,0.00,1,'2026-01-22 14:51:37'),(826,'C04196','CABLE LSOH-90+ CAB. 25 MM² 750V NEGRO',2,0.00,1,'2026-01-22 14:51:37'),(827,'C04197','CABLE LSOH-90+ CAB. 35 MM² 750V NEGRO',2,0.00,1,'2026-01-22 14:51:37'),(828,'C04198','CABLE LSOH-90+ CAB. 25 MM² 750V VERDE/AMARILLO',2,0.00,1,'2026-01-22 14:51:37'),(829,'C04199','CABLE CAAI AUTOPORTANTE 3X50 MM + P35MM',2,0.00,1,'2026-01-22 14:51:37'),(830,'C04200','SOPORTE MW 1MT X 3° TIPO SHAKIRA CON BRAZOS DE 40CM',5,0.00,1,'2026-01-22 14:51:37'),(831,'C04201','TABLERO GENERAL 304 3F-220',1,0.00,1,'2026-01-22 14:51:37'),(832,'C04202','POSTE CAC 15/600/2/225/450',1,0.00,1,'2026-01-22 14:51:37'),(833,'C04203','POSTE CAC 11/350/2/165/330 + PERILLA',1,0.00,1,'2026-01-22 14:51:37'),(834,'C04204','POSTE CAC 15/400/225/450',1,0.00,1,'2026-01-22 14:51:37'),(835,'C04205','LUZ DE EMERGENCIA',1,0.00,1,'2026-01-22 14:51:37'),(836,'C04206','CABLE UTP CAT6 COLOR NEGRO',2,0.00,1,'2026-01-22 14:51:37'),(837,'C04207','ABRAZADERA 144-216',1,0.00,1,'2026-01-22 14:51:37'),(838,'C04208','ABRAZADERA 143 -165',1,0.00,1,'2026-01-22 14:51:37'),(839,'C04209','CUADERNO PUBLICITARIO A4',1,0.00,1,'2026-01-22 14:51:37'),(840,'C04210','LAPICERO DE METAL CON CLIP, CINTILLO Y GRABADO LASER',1,0.00,1,'2026-01-22 14:51:37'),(841,'C04211','LAPICERO DE METAL CON CLIP, CINTILLO Y GRABADO LASER - PERSONALIZADO',1,0.00,1,'2026-01-22 14:51:37'),(842,'C04212','LAPICERO PVC CON IMPRESIÓN',1,0.00,1,'2026-01-22 14:51:37'),(843,'C04213','PELOTA ANTIESTRES',1,0.00,1,'2026-01-22 14:51:37'),(844,'C04214','TOMATODO MUG 500ML SUBLIMADO',1,0.00,1,'2026-01-22 14:51:37'),(845,'C04215','MARTIL RF TUBO 2 1/2\" X 1.4M + 1 PLACA BASE',1,0.00,1,'2026-01-22 14:51:37'),(846,'C04216','CABLE UTP CAT6 APANTALLADO COLOR NEGRO',1,0.00,1,'2026-01-22 14:51:37'),(847,'C04217','BREAKER TRIPOLAR 125 AMP',1,0.00,1,'2026-01-22 14:51:37'),(848,'C04218','POSTE CAC 11/400/2/165/330 + PERILLA',1,0.00,1,'2026-01-22 14:51:37'),(849,'C04219','LAPTOP ASUS TUF F15 FX507ZC4-HN002 CORE I7-12700H 15.6 FHD 144HZ RAM 16GB DDR4 SSD 512GB RTX3050',1,0.00,1,'2026-01-22 14:51:37'),(850,'C04220','LAPTOP HP 250 G9 CORE I7 1255U RAM 16GB SSD 512GB 15.6\" HD',1,0.00,1,'2026-01-22 14:51:37'),(851,'C04221','PIZARRA 1.60X1.20 MT DE CRISTAL TEMPLADO PERSONALIZADA',1,0.00,1,'2026-01-22 14:51:37'),(852,'C04222','TABLERO HERMETICO DE 300X300X200 MM CON SOPORTE',1,0.00,1,'2026-01-22 14:51:37'),(853,'C04223','SIKADUR 32 GEL',1,0.00,1,'2026-01-22 14:51:37'),(854,'C04224','TORNILLO CAMISETA ZINC EXP MAMUT 3/16° X 54MM H3/8°',1,0.00,1,'2026-01-22 14:51:37'),(855,'C04225','PAR DE BRAZOS DE 30CM TIPO H EN PIVOTANTE ROLDANA PARA MW 1.20M',5,0.00,1,'2026-01-22 14:51:37'),(856,'C04226','PAR DE BRAZOS REFORZADOS DE 30CM PARA MW DE 1.20M P/TORRE AUTOSOPORTADA',5,0.00,1,'2026-01-22 14:51:37'),(857,'C04227','PAR DE BRAZOS REFORZADOS DE 40CM X 2.1/2\" X 3MM TIPO SANDWICH PATA DE GALLO CON MONTANTE 4\" P/ANTENA MW DE 1.20M',5,0.00,1,'2026-01-22 14:51:37'),(858,'C04228','PAR DE BRAZOS REFORZADOS REGULABLES DE 40CM X 2.1/2\" X 3MM TIPO SANDIWCH CON PATA DE GALLO MONTANTE 4\" P/ANTENA MW DE 0.60M',5,0.00,1,'2026-01-22 14:51:37'),(859,'C04229','PAR DE BRAZOS REFORZADOS REGULABLES DE 40CM X 2.1/2\" X 3MM TIPO SANDWICH CON PATA DE GALLO MONTANTE 4\" P/ANTENA MW DE 0.30M',5,0.00,1,'2026-01-22 14:51:37'),(860,'C04230','SOPORTE 1.20M X 4\" X 3MM CON BRAZOS REGULABLES DE 50CM X 2.1/2\" X 3MM CON UBOLT A TUBO DE 4\" A 4\" P/ANTENA MW DE 1.20M',5,0.00,1,'2026-01-22 14:51:37'),(861,'C04231','SOPORTE REFORZADO DE 1.20M X 3\" X 3MM CON BRAZOS REGULABLES DE 50CM X 2.1/2\" X 3MM CON UBOLT A TUBO DE 6\" A 3\"  P/ANTENA MW DE 0.60M',5,0.00,1,'2026-01-22 14:51:37'),(862,'C04232','SOPORTE REFORZADO DE 1.20M X 4\" X 3MM CON LARGUEROS DE 3.50M X CANAL 4” CON BRAZOS DE 30CM PARA MW DE 1.20 EN P/TORRE AUTOSPORTADA',5,0.00,1,'2026-01-22 14:51:37'),(863,'C04233','SOPORTE TIPO SHAKIRA CON DIAMETRO 35CM Y BRAZOS FIJO DE 40CM X 2.1/2\" X 3MM P/ANTENA MW DE 0.30M P/TORRE MONOPOLO',5,0.00,1,'2026-01-22 14:51:37'),(864,'C04234','SOPORTE TIPO SHAKIRA CON DIAMETRO 42.50CM Y BRAZOS FIJO DE 40CM X 2.1/2\" X 3MM P/ANTENA MW DE 0.60M P/TORRE MONOPOLO',5,0.00,1,'2026-01-22 14:51:37'),(865,'C04235','LAPTOP LENOVO IDEAPAD SLIM 3 CORE I5-12450H, RAM 8GB, SSD512GB, 15.6\" FHD, FREE DOS',1,0.00,1,'2026-01-22 14:51:37'),(866,'C04236','POSTE CAC 11/350/150/315',1,0.00,1,'2026-01-22 14:51:37'),(867,'C04237','POSTE CAC 9/400/150/285',1,0.00,1,'2026-01-22 14:51:37'),(868,'C04238','AMARRE PREFORMADO NEGRO 5/16',1,0.00,1,'2026-01-22 14:51:37'),(869,'C04239','BANDEJA ODF DE 24 HILOS',1,0.00,1,'2026-01-22 14:51:37'),(870,'C04240','ESTANTE DE MELAMINA 0.6 X 0.40 X 0.9 M',1,0.00,1,'2026-01-22 14:51:37'),(871,'C04241','ESTANTE DE MELAMINA 2 X 0.40 X 1.8 M',1,0.00,1,'2026-01-22 14:51:37'),(872,'C04242','MESA PARA OFICINA CON CAPACIDAD P/8 PERSONAS INCLUYE INSTALACION',1,0.00,1,'2026-01-22 14:51:37'),(873,'C04243','SILLA EJECUTIVA DALI',1,0.00,1,'2026-01-22 14:51:37'),(874,'C04244','SILLA FONE ULTRA GERENTE BASE CROMADA 60 CM',1,0.00,1,'2026-01-22 14:51:37'),(875,'C04245','CAJA MULTIFUNCIONAL CON ENTRADAS DIVERSAS',1,0.00,1,'2026-01-22 14:51:37'),(876,'C04246','LAPTOP HP 255 G10 RYZEN 7 7730U MEMORIA 16GB SSD512GB 15.6\" FREEDOS',1,0.00,1,'2026-01-22 14:51:37'),(877,'C04247','CASE CORSAIR 3000D AIRFLOW RGB WHITE SIN FUENTE VIDRIO TEMPLADO USB 3.2',1,0.00,1,'2026-01-22 14:51:37'),(878,'C04248','COMBO LOGITECH MK120 ( 920-004428 ) TECLADO+MOUSE',1,0.00,1,'2026-01-22 14:51:37'),(879,'C04249','ESTABILIZADOR FORZA 8 TOMAS ( FVR-2202 ) 2200VA',1,0.00,1,'2026-01-22 14:51:37'),(880,'C04250','FUENTE 1000W CORSAIR RME RM1000E 80 PLUS GOLD FULLY MODULAR',1,0.00,1,'2026-01-22 14:51:37'),(881,'C04251','MEMORIA 16GB DDR5 XPG  BUS 5600MHZ LANCER BLADE WHITE',1,0.00,1,'2026-01-22 14:51:37'),(882,'C04252','MONITOR LG 27\" ( 27MS500-B ) PANEL IPS | 2 HDMI | 100HZ 5MS 1980X1080',1,0.00,1,'2026-01-22 14:51:37'),(883,'C04253','PLACA GIGABYTE Z790 S DDR4 LGA1700 (RJ-45) WIFI BLUETOOTH',1,0.00,1,'2026-01-22 14:51:37'),(884,'C04254','PROCESADOR INTEL CORE I7 12700KF (BX8071512700KF) 3.6/5.00GHZ|25 MB CACHE L3| LGA 1700',1,0.00,1,'2026-01-22 14:51:37'),(885,'C04255','SISTEMA DE ENFRIAMIENTO LIQUIDO COOLER MASTER MASTERLIQUID ML240 ILLUSION',1,0.00,1,'2026-01-22 14:51:37'),(886,'C04256','SSD SOLIDO 1TB KINGSTON NV3  NVME PCIE GEN 4.0',1,0.00,1,'2026-01-22 14:51:37'),(887,'C04257','TARJETA DE VIDEO GIGABYTE RTX 4070 SUPER 12GB GDDR6X 192BITS OC GAMING',1,0.00,1,'2026-01-22 14:51:37'),(888,'C04258','PAR DE BRAZOS FIJOS DE 40CM X 2.1/2\" PARA TUBO DE 4\" A 4\"',5,0.00,1,'2026-01-22 14:51:37'),(889,'C04259','PAR DE BRAZOS REFORZADOS REGULABLES DE 40CM X 2.1/2\" X 3MM TIPO SANDWICH CON PATA DE GALLO MONTANTE 4\" P/ANTENA MW DE 0.60M',5,0.00,1,'2026-01-22 14:51:37'),(890,'C04260','PAR DE BRAZOS REGULABLES DE 25CM A 40CM X 2.1/2\" X 3MM HACIA TUBO DE 4\" P/ANTENA MW DE 0.30M',5,0.00,1,'2026-01-22 14:51:37'),(891,'C04261','PAR DE LARGUEROS DE 3.50M X CANAL 4” CON BRAZOS DE 30CM PARA MW DE 1.20M',5,0.00,1,'2026-01-22 14:51:37'),(892,'C04262','PAR DE PLANCHAS DE 20X20X1/4 PARA PIVOTANTE DE 2\" A TUBO DE 4\"',5,0.00,1,'2026-01-22 14:51:37'),(893,'C04263','SOPORTE REFORZADO DE 1.20M X 4\" X 3MM CON LARGUEROS DE 4.00M X CANAL 4” CON BRAZOS DE 30CM PARA MW DE 1.20',5,0.00,1,'2026-01-22 14:51:37'),(894,'C04264','ESCALERA VERTICAL 2 PARTES GALVANIZADO DE 9.40M COM TUBO CUADRADO 2°X 2° X 3MM CON LÍNEA DE VIDA 3/8 ACERADO PARA POSTE 18M',5,0.00,1,'2026-01-22 14:51:37'),(895,'C04265','ESCALERA VERTICAL 2 PARTES GALVANIZADO DE 6.40M COM TUBO CUADRADO 2°X 2° X 3MM CON LÍNEA DE VIDA 3/8 ACERADO PARA POSTE 12M',5,0.00,1,'2026-01-22 14:51:37'),(896,'C04266','POSTE CAC 11/300/2/165/330',1,0.00,1,'2026-01-22 14:51:37'),(897,'C04267','ESTRUCTURA METÁLICA GALVANIZADO P/RADOMO 3 X 3 M LARGO CON ANGULOS 2 2\" X3/16 X',1,0.00,1,'2026-01-22 14:51:37'),(898,'C04268','ESTRUCTURA POLÍMERO CON FIBRA DE VIDRIO RODMO CON DIÁMETRO = 3 M X ALTURA 3.30 M',1,0.00,1,'2026-01-22 14:51:37'),(899,'C04269','SOPORTE RF 3M X 2.5° CON BRAZOS DE 50CM P/ROSETA',5,0.00,1,'2026-01-22 14:51:37'),(900,'C04270','POSTE FIBRA DE VIDRIO 8MTS/200KG',1,0.00,1,'2026-01-22 14:51:37'),(901,'C04271','POLO, SHORT Y PAR DE MEDIA DEPORTIVA',5,0.00,1,'2026-01-22 14:51:37'),(902,'C04272','PATCH CORD F.O. INDOOR LC/UPC-LC/UPC 3MTS',1,0.00,1,'2026-01-22 14:51:37'),(903,'C04273','TUBO RF 2.5° X 3 MTS',1,0.00,1,'2026-01-22 14:51:37'),(904,'C04274','SOPORTE DE 3M X 2.5° CON BRAZOS FIJOS DE 60CM PARA MÁSTIL DE 4°',1,0.00,1,'2026-01-22 14:51:37'),(905,'C04275','PAR DE BRAZO RF DE 60CM DE TUBO A TUBO DE 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(906,'C04276','LAPTOP HP 250 G10 INTEL CORE I7-1355U 16GB SSD512GB 15.6\" W11PRO',1,0.00,1,'2026-01-22 14:51:37'),(907,'C04277','LAPTOP LENOVO IDEAPAD SLIM 3 CORE I5-12450H, RAM 16GB, SSD 512GB',1,0.00,1,'2026-01-22 14:51:37'),(908,'C04278','POSTE DE FIBRA DE VIDRIO(PRFV) 15MTS/400KG',1,0.00,1,'2026-01-22 14:51:37'),(909,'C04279','POSTE DE FIBRA DE VIDRIO(PRFV) 9MTS/250KG',1,0.00,1,'2026-01-22 14:51:37'),(910,'C04280','CABLE LSOH-90+ CAB. 25 MM² 750V AMARILLO',2,0.00,1,'2026-01-22 14:51:37'),(911,'C04281','TABLERO MDP 3F+N-380/220VAC-60HZ ROOFTOP',1,0.00,1,'2026-01-22 14:51:37'),(912,'C04282','TABLERO PDP 3F+N+T 380/220VAC-60HZ GREENFIED',1,0.00,1,'2026-01-22 14:51:37'),(913,'C04283','TABLERO PDP 3F+N-380/220VAC-60HZ ROOFTOP',1,0.00,1,'2026-01-22 14:51:37'),(914,'C04284','ESCALERILLA GALVANIZADA DE 0.30M',2,0.00,1,'2026-01-22 14:51:37'),(915,'C04285','ESCUADRA 90° GALVANIZADA 5 x 5 CM PARA UNION DE ESCALERILLA VERTICAL Y HORIZONTAL',1,0.00,1,'2026-01-22 14:51:37'),(916,'C04286','ABRAZADERA DOBLE OREJA DE 2\"',1,0.00,1,'2026-01-22 14:51:37'),(917,'C04287','ABRAZADERA TIPO U DOBLE OREJA DE 1 1/2\"',1,0.00,1,'2026-01-22 14:51:37'),(918,'C04288','BORNERA DE PASO 25MM',1,0.00,1,'2026-01-22 14:51:37'),(919,'C04289','BREAKER TRIPOLAR 80 AMP',1,0.00,1,'2026-01-22 14:51:37'),(920,'C04290','CAJA DE PASO 15X15X10 METALICO',1,0.00,1,'2026-01-22 14:51:37'),(921,'C04291','CAJA DE PASO 15X15X15 METALICO',1,0.00,1,'2026-01-22 14:51:37'),(922,'C04292','CODO EMT 1 1/2\"',1,0.00,1,'2026-01-22 14:51:37'),(923,'C04293','CODO PVC 1 1/2\"',1,0.00,1,'2026-01-22 14:51:37'),(924,'C04294','CONECTOR EMT DE 2\"',1,0.00,1,'2026-01-22 14:51:37'),(925,'C04295','CURVA EMT DE 2\"',1,0.00,1,'2026-01-22 14:51:37'),(926,'C04296','MANGA TERMOCONTRAIBLE 25MM NEGRO',2,0.00,1,'2026-01-22 14:51:37'),(927,'C04297','MANGA TERMOCONTRAIBLE 25MM ROJO',2,0.00,1,'2026-01-22 14:51:37'),(928,'C04298','PRENSA ESTOPA METALICA 1 1/2°',1,0.00,1,'2026-01-22 14:51:37'),(929,'C04299','PRENSA ESTOPA METALICA 2°',1,0.00,1,'2026-01-22 14:51:37'),(930,'C04300','TACO DE EXPANSION DE 3/8\" + PERNO + TUERCA',1,0.00,1,'2026-01-22 14:51:37'),(931,'C04301','TARUGO AZUL + TORNILLO',1,0.00,1,'2026-01-22 14:51:37'),(932,'C04302','TERMINAL T/PUNTA 35',1,0.00,1,'2026-01-22 14:51:37'),(933,'C04303','TUBO CORRUGADO PESADO 1 1/2°',2,0.00,1,'2026-01-22 14:51:37'),(934,'C04304','TUBO EMT 1 1/2\" X 3 MT',1,0.00,1,'2026-01-22 14:51:37'),(935,'C04305','TUBO PVC 1 1/2\" X 3MT',1,0.00,1,'2026-01-22 14:51:37'),(936,'C04306','UNION PVC 1 1/2\"',1,0.00,1,'2026-01-22 14:51:37'),(937,'C04307','SOPORTE P/BANDEJA 25X20CM',1,0.00,1,'2026-01-22 14:51:37'),(938,'C04308','POSTE DE FIBRA DE VIDRIO(PRFV) 15MTS/350KG',1,0.00,1,'2026-01-22 14:51:37'),(939,'C04309','ABRAZADERA JOTA 2°',1,0.00,1,'2026-01-22 14:51:37'),(940,'C04310','ESPARRAGO 3/8',1,0.00,1,'2026-01-22 14:51:37'),(941,'C04311','TUBO CORRUGADO PESADO 2°',2,0.00,1,'2026-01-22 14:51:37'),(942,'C04312','TUBO EMT DE 2°',1,0.00,1,'2026-01-22 14:51:37'),(943,'C04313','TUERCA 3/8',1,0.00,1,'2026-01-22 14:51:37'),(944,'C04314','UNION EMT DE 2°',1,0.00,1,'2026-01-22 14:51:37'),(945,'C04315','POSTE DE FIBRA DE VIDRIO(PRFV) 11MTS/350KG',1,0.00,1,'2026-01-22 14:51:37'),(946,'C04316','POSTE DE FIBRA DE VIDRIO(PRFV) 18MTS/500KG SECC 2 PIEZAS',1,0.00,1,'2026-01-22 14:51:37'),(947,'C04317','LUMINARIA ALUMBRADO PUBLICO TR-126-150W 10KV + FOTOCELDA',1,0.00,1,'2026-01-22 14:51:37'),(948,'C04318','MANTENIMIENTO GENERAL DE CARRETA CON TORRE DE ANTENA',1,0.00,1,'2026-01-22 14:51:37'),(949,'C04319','BARRA BORNERA DE 12 FORADOS',1,0.00,1,'2026-01-22 14:51:37'),(950,'C04320','SOPORTE DE 1M X 2.5\" PARA MW DE 0.60M CON BRAZOS DE 40CM EN TORRE VENTADA',1,0.00,1,'2026-01-22 14:51:37'),(951,'C04321','SOPORTE RF 3M X 2.5\" PARA ANTENA CON BRAZOS DE 40CM',5,0.00,1,'2026-01-22 14:51:37'),(952,'C04322','CABLE LSOH-90+ CAB. 16 MM² 750V NEGRO',2,0.00,1,'2026-01-22 14:51:37'),(953,'C04323','MASTIL DE 3.08M X 2.5\" CON BASE SUPERIOR E INFERIOR DE 20CM X 25CM',5,0.00,1,'2026-01-22 14:51:37'),(954,'C04324','CABLE COAXIAL DE 1/2\" SUPERFLEX',2,0.00,1,'2026-01-22 14:51:37'),(955,'C04325','ADECUACIÓN TIPO L DE 17CM X 5CM CON PLANCHA SOLDADA DE 10CM X 5CM',1,0.00,1,'2026-01-22 14:51:37'),(956,'C04326','ESPÁRRAGOS 1/2\" X 14CM',1,0.00,1,'2026-01-22 14:51:37'),(957,'C04327','PAR DE BRAZOS DE 30CM PARA POSTE',1,0.00,1,'2026-01-22 14:51:37'),(958,'C04328','PERNOS 1/2\" X 4\"',1,0.00,1,'2026-01-22 14:51:37'),(959,'C04329','PLANCHA DE AGARRE DE 14CM X 5CM',1,0.00,1,'2026-01-22 14:51:37'),(960,'C04330','PROTECCIÓN DE POSTE CON PUAS DE 3/8 SUPERIOR E INFERIOR PARA POSTE',1,0.00,1,'2026-01-22 14:51:37'),(961,'C04331','TABLERO PDP 600x400x200MM',1,0.00,1,'2026-01-22 14:51:37'),(962,'C04332','TABLERO INTEGRADO 2F-220VAC-60HZ',1,0.00,1,'2026-01-22 14:51:37'),(963,'C04333','CONECTOR EMT 1\"',1,0.00,1,'2026-01-22 14:51:37'),(964,'C04334','TUBO EMT 1\"',1,0.00,1,'2026-01-22 14:51:37'),(965,'C04335','UNION EMT 1\"',1,0.00,1,'2026-01-22 14:51:37'),(966,'C04336','POSTE CAC 15/400/210/435',1,0.00,1,'2026-01-22 14:51:37'),(967,'C04337','SOPORTE RF 3M X 2.5\" CON 2 BRAZOS REGULABLES DE 50CM A 70CM DE TUBO A TUBO 2.5\"',1,0.00,1,'2026-01-22 14:51:37'),(968,'C04338','PAR DE BRAZOS REGULABLES DE 60CM PARA MW EN TORRE AUTOSOPORTADA',1,0.00,1,'2026-01-22 14:51:37'),(969,'C04339','CABLE RET 20M CONECTOR DB9(M) - AISG 8(F)',1,0.00,1,'2026-01-22 14:51:37'),(970,'C04340','ARO 215 75X15 MT',1,0.00,1,'2026-01-22 14:51:37'),(971,'C04341','LLANTA 215 75X15 AT',1,0.00,1,'2026-01-22 14:51:37'),(972,'C04342','TUBO PVC SAP 1\" X 3MT',1,0.00,1,'2026-01-22 14:51:37'),(973,'C04343','CURVA PVC DE 2\"',1,0.00,1,'2026-01-22 14:51:37'),(974,'C04344','ESPIRAL DE 10 MM TRANSPARENTE',6,0.00,1,'2026-01-22 14:51:37'),(975,'C04345','SOPORTE RF 3.5M X 2.5\" CON BRAZOS TUBO A TUBO DE 60CM',5,0.00,1,'2026-01-22 14:51:37'),(976,'C04346','CINTILLO METALICO 40CM',6,0.00,1,'2026-01-22 14:51:37'),(977,'C04347','ESCALERA VERTICAL 2 PARTES GALVANIZADO DE 9.80M CON TUBO CUADRADO 2.5\"X2.5\"X3MM CON LINEA DE VIDA 3/8 ACERADO BRAZOS DE 15CM',1,0.00,1,'2026-01-22 14:51:37'),(978,'C04348','MASTIL RF 2M X 4” X 3MM CON BASE A ADOSAR EN PARTE SUPERIOR DE TORRE MONOPOLO',1,0.00,1,'2026-01-22 14:51:37'),(979,'C04349','SOPORTE RF 3M X 2.5” CON BRAZOS DE 40CM DE MÁSTIL 4” A 2.5”',1,0.00,1,'2026-01-22 14:51:37'),(980,'C04350','AMARRE PREFORMADO 144 H FO D: 15.7MM',1,0.00,1,'2026-01-22 14:51:37'),(981,'C04351','CLEVIS PORTA LINEA 53-1',1,0.00,1,'2026-01-22 14:51:37'),(982,'C04352','CINTILLO CV 500 BLANCO',6,0.00,1,'2026-01-22 14:51:37'),(983,'C04353','ETIQUETA WOW MORADA',1,0.00,1,'2026-01-22 14:51:37'),(984,'C04355','TABLERO PDP 2Ø 3F-220VAC-60HZ GREENFIED',1,0.00,1,'2026-01-22 14:51:37'),(985,'C04356','MONOPOSTE H= 15 M',1,0.00,1,'2026-01-22 14:51:37'),(986,'C04357','SOPORTE BAT BLADE 1.8M X 2° CON BRAZOS DE 40CM',5,0.00,1,'2026-01-22 14:51:37'),(987,'C04358','SOPORTE RF 3M X 2” CON BRAZOS FIJOS DE 60CM DE TUBO DE 2.5\" A ANGULO DE TORRE DE 2.5\"',1,0.00,1,'2026-01-22 14:51:37'),(988,'C04359','TABLERO METALICO PLANCHA LAF TRIFASICO TIPO TD2 PARA TRAFO DE 100 KVA',1,0.00,1,'2026-01-22 14:51:37'),(989,'C04360','TABLERO METALICO PLANCHA LAF TRIFASICO TIPO TD3 PARA TRAFO DE 160 KVA',1,0.00,1,'2026-01-22 14:51:37'),(990,'C04361','CABLE AUTOPORTANTE CAAI-S 3X50 + 2X25 + P. ACERO',2,0.00,1,'2026-01-22 14:51:37'),(991,'C04362','SOPORTE DE 2.5\" X 3M CON PLANCHA DE 20X20CM PARA SUJETAR ANGULO DE 2\"(5CM) HACIA TUBO DE 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(992,'C04363','SOPORTE DE 2.5\" X 3M CON BRAZOS ANGULO DE 40CM X 3/16(4.5MM) SUJETAS EN CANAL \"C\" DE 4\"X 1.5\"HACIA TUBO DE 2.5”',5,0.00,1,'2026-01-22 14:51:37'),(993,'C04364','SOPORTE DE 2.5\" X 3M CON BRAZOS ANGULO DE 60CM X 3/16(4.5MM) PARA ROLDANA DE 2\"',5,0.00,1,'2026-01-22 14:51:37'),(994,'C04365','SOPORTE DE 2.5\" X 3M CON BRAZOS ANGULO DE 40CM X 3/16(4.5MM) PARA TORRE VENTADA  MONTANTE 1 1/2\"',5,0.00,1,'2026-01-22 14:51:37'),(995,'C04366','CABLE AUTOPORTANTE DE ALUMINIO CAAI-S 3X35 + 2X16 + P. ACERO',2,0.00,1,'2026-01-22 14:51:37'),(996,'C04367','TABLERO PDP 3F-220VAC-60HZ ESPECIAL',1,0.00,1,'2026-01-22 14:51:37'),(997,'C04368','TABLERO METALICO PLANCHA LAF TRIFASICO TIPO TD2 PARA TRAFO DE 50 KVA',1,0.00,1,'2026-01-22 14:51:37'),(998,'C04369','TABLERO METALICO PLANCHA LAF TRIFASICO TIPO TD3 PARA TRAFO DE 250 KVA',1,0.00,1,'2026-01-22 14:51:37'),(999,'C04370','MB AR H610M-HDV/M.2 R2.0 DDR4',1,0.00,1,'2026-01-22 14:51:37'),(1000,'C04371','MEM RAM 8G FURY BEAST 3.60G D4',1,0.00,1,'2026-01-22 14:51:37'),(1001,'C04372','PROC INT CORE I5-12400F 2.50GZ',1,0.00,1,'2026-01-22 14:51:37'),(1002,'C04373','SSD WD SN350 1TB GREEN NVME',1,0.00,1,'2026-01-22 14:51:37'),(1003,'C04374','ABRAZADERA T/ GOTA 3\"',1,0.00,1,'2026-01-22 14:51:37'),(1004,'C04375','CAJA PASO METALICA 40X40X20',1,0.00,1,'2026-01-22 14:51:37'),(1005,'C04376','CODO 90 EMT 3\"',1,0.00,1,'2026-01-22 14:51:37'),(1006,'C04377','CONECTOR 4.3 MACHO P/CABLE DE 1/2° RIGIDO',1,0.00,1,'2026-01-22 14:51:37'),(1007,'C04378','CONECTOR EMT 3\"',1,0.00,1,'2026-01-22 14:51:37'),(1008,'C04379','ARANDELA 3/8',1,0.00,1,'2026-01-22 14:51:37'),(1009,'C04380','PRENSA ESTOPA METALICA 3\"',1,0.00,1,'2026-01-22 14:51:37'),(1010,'C04381','TUBERIA CONDUIT FLEXIBLE 3°',2,0.00,1,'2026-01-22 14:51:37'),(1011,'C04382','TUBO EMT 3\"',1,0.00,1,'2026-01-22 14:51:37'),(1012,'C04383','UNION EMT 3\"',1,0.00,1,'2026-01-22 14:51:37'),(1013,'C04384','EXTRACTOR HELICOCENTRIFUGO 6\" TD 800/200 SYP',1,0.00,1,'2026-01-22 14:51:37'),(1014,'C04385','BACKPACK TEROS CITY BEIGE',1,0.00,1,'2026-01-22 14:51:37'),(1015,'C04386','DTXM 64GB NEON GREEN',1,0.00,1,'2026-01-22 14:51:37'),(1016,'C04387','MOUSE STD WIRELESS TE1219 PPL',1,0.00,1,'2026-01-22 14:51:37'),(1017,'C04388','PARLANTES TEROS TE-6032N BT',1,0.00,1,'2026-01-22 14:51:37'),(1018,'C04389','CASE MICRO ATX TE1034 250W BK',1,0.00,1,'2026-01-22 14:51:37'),(1019,'C04390','MB AS H610M-K D4 CSM SVL DDR4',1,0.00,1,'2026-01-22 14:51:37'),(1020,'C04391','MOUSE STD WIRELESS TE1218 PK',1,0.00,1,'2026-01-22 14:51:37'),(1021,'C04392','PROC INT CORE I5-12400 2.50GHZ',1,0.00,1,'2026-01-22 14:51:37'),(1022,'C04393','SSD 1T TG MP33 M.2 NVME PCI3X4',1,0.00,1,'2026-01-22 14:51:37'),(1023,'C04394','SOPORTE RF 3M X 2.5\" CON 2 BRAZOS REGULABLES DE 0.50CM DE TUBO DE 4\" A 2 1/2\" X 3MM',5,0.00,1,'2026-01-22 14:51:37'),(1024,'C04395','SOPORTE RF 3M X 2.5\" CON 2 BRAZOS REGULABLES DE 0.35CM DE TUBO DE 4\" A 2 1/2\" X 3MM',5,0.00,1,'2026-01-22 14:51:37'),(1025,'C04396','SOPORTE RF 3M X 2.5\" CON 2 BRAZOS REGULABLES DE 0.20CM DE TUBO DE 4\" A 2 1/2\" X 3MM',5,0.00,1,'2026-01-22 14:51:37'),(1026,'C04397','SOPORTE RF 3M X 2.5\" CON 2 BRAZOS REGULABLES DE 0.55CM DE TUBO DE 4\" A 2 1/2\" X 3MM',5,0.00,1,'2026-01-22 14:51:37'),(1027,'C04398','CABLE AUTOPORTANTE CAAI-S 3X70 + 2X35 + P. ACERO',2,0.00,1,'2026-01-22 14:51:37'),(1028,'C04399','ESCALERA PEATONAL H=26M T. 3\"X2M',1,0.00,1,'2026-01-22 14:51:37'),(1029,'C04400','MODULO PARA MONOPOLO H=6M',1,0.00,1,'2026-01-22 14:51:37'),(1030,'C04401','21 CANALETAS DE PL GALV. DE 0.9MM + 1 CANALETA BASE DE PL GALV. DE 0.9MM',5,0.00,1,'2026-01-22 14:51:37'),(1031,'C04402','POSTE CAC 8/300/150/270',1,0.00,1,'2026-01-22 14:51:37'),(1032,'C04403','TABLERO NAT 2F-220V AC-60HZ',1,0.00,1,'2026-01-22 14:51:37'),(1033,'C04404','TABLERO NAT 3F-220V AC-60HZ',1,0.00,1,'2026-01-22 14:51:37'),(1034,'C04405','PATCH CORD SIMPLEX FO OUTDOOR LC/UPC-SC/UPC 5MTS',1,0.00,1,'2026-01-22 14:51:37'),(1035,'C04406','TABLERO INTEGRADO 3F-220VAC-60HZ',1,0.00,1,'2026-01-22 14:51:37'),(1036,'C04407','TRANSFORMADOR BIFASICO BAJA TENSION ACEITE ELEVADOR 15 KVA, 2Ø, 60 HZ, 220±2x2.5% / 900 V',1,0.00,1,'2026-01-22 14:51:37'),(1037,'C04408','TRANSFORMADOR BIFASICO BAJA TENSION ACEITE REDUCTOR 15 KVA, 2Ø, 60 HZ, 900±2.x2.5% / 220 V',1,0.00,1,'2026-01-22 14:51:37'),(1038,'C04409','TRANSFORMADOR DE DISTRIBUCIÓN MONOFÁSICO DE 25 KVA FASE-TIERRA',1,0.00,1,'2026-01-22 14:51:37'),(1039,'C04410','ESCALERILLA GALVANIZADA 0.40 X 3M',1,0.00,1,'2026-01-22 14:51:37'),(1040,'C04411','ESCALERILLA GALVANIZADA 0.40 X 1.6M',1,0.00,1,'2026-01-22 14:51:37'),(1041,'C04412','L GALVANIZADA 10CM',1,0.00,1,'2026-01-22 14:51:37'),(1042,'C04413','CABLE CAAI 3x35+NA25 MM2 0,6/1 kV NEGRO',2,0.00,1,'2026-01-22 14:51:37'),(1043,'C04414','CABLE THW-90 BH 1x25 MM2 450/750 V NEGRO',2,0.00,1,'2026-01-22 14:51:37'),(1044,'C04415','CABLE CAAI 2x25+NA25 MM2 0.6/1 KV NEGRO',2,0.00,1,'2026-01-22 14:51:37'),(1045,'C04416','PAR DE BRAZO FIJOS DE 60CM CON OJO CHINO A ROSETA',5,0.00,1,'2026-01-22 14:51:37'),(1046,'C04417','TABLERO NAT PDP INTEGRADO 3F 220 VAC 60HZ',1,0.00,1,'2026-01-22 14:51:37'),(1047,'C04418','POSTE CAC 18/600/180/450',1,0.00,1,'2026-01-22 14:51:37'),(1048,'C04419','PAR DE BRAZO REGULABLE DE 70CM TUBO A MONTANTE',5,0.00,1,'2026-01-22 14:51:37'),(1049,'C04420','CABLE N2XSY 1X70MM2 18/30KV COBRE/XLPE/CU/PVC ROJO',2,0.00,1,'2026-01-22 14:51:37'),(1050,'C04421','TABLERO MDP 3F-220VAC-60HZ 30KW ROOFTOP',1,0.00,1,'2026-01-22 14:51:37'),(1051,'C04422','TABLERO PDP 3F-220VAC-60HZ 30KW ROOFTOP',1,0.00,1,'2026-01-22 14:51:37'),(1052,'C04423','MANGA TERMOCONTRAIBLE 16MM NEGRO',2,0.00,1,'2026-01-22 14:51:37'),(1053,'C04424','PAR DE BRAZO DE 70 CM T/PATA DE GALLO',5,0.00,1,'2026-01-22 14:51:37'),(1054,'C04425','ESCALERA VERTICAL 2 PARTES GALVANIZADO DE 9.40M COM TUBO CUADRADO 3°X 3° X 3MM CON LÍNEA DE VIDA 3/8 ACERADO PARA POSTE 15M',5,0.00,1,'2026-01-22 14:51:37'),(1055,'C04426','SOPORTE RF 1.5M X 2° CON BRAZOS DE 30CM',5,0.00,1,'2026-01-22 14:51:37'),(1056,'C04427','SOPORTE BAT BLADE 1.5M X 2° CON BRAZOS DE 30CM',5,0.00,1,'2026-01-22 14:51:37'),(1057,'C04428','SOPORTE RF 3M X 2.5\" CON 2 BRAZOS REGULABLES DE 1M A TUBO 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1058,'C04429','SOPORTE RF 3M X 2.5\" CON 2 BRAZOS DE 20CM X 20CM T/SANDWICH A MASTIL',5,0.00,1,'2026-01-22 14:51:37'),(1059,'C04430','PAR DE BRAZO FIJO DE 60CM DE MONTANTE DE 1.5\"A TUBO DE 2\"',5,0.00,1,'2026-01-22 14:51:37'),(1060,'C04431','SOPORTE RF 3.5M X 3\" CON BRAZOS REGULABLES DE 40 A 60CM A TUBO DE 4\" A 3\" X 3MM',5,0.00,1,'2026-01-22 14:51:37'),(1061,'C04432','SOPORTE RF 3M X 2.5° CON BRAZOS DE 35CM A ROSETA',5,0.00,1,'2026-01-22 14:51:37'),(1062,'C04433','SOPORTE RF 3.5M X 2.5° CON BRAZOS DE 40CM A ROSETA',5,0.00,1,'2026-01-22 14:51:37'),(1063,'C04434','PATCH CORD DUPLEX FO SM INDOOR LC/UPC-LC/UPC 10MTS',1,0.00,1,'2026-01-22 14:51:37'),(1064,'C04435','UNION TUBULAR 10MM',1,0.00,1,'2026-01-22 14:51:37'),(1065,'C04436','CABLE CAAI 3X35+1x16NA25 MM2 0,6/1 KV',2,0.00,1,'2026-01-22 14:51:37'),(1066,'C04437','VANO DE 1 M X 1 M CON MARCO DE ANGULO DE 1\" x 1 1/2\" x 1/8\" + MALLA COCADA DE 2.5 CM x 2.5 CM + MARCO GALVANIZADO ARMABLE INTERIOR CON PLATINA DE 1 1/2\" x 1/8\"',5,0.00,1,'2026-01-22 14:51:37'),(1067,'C04438','PERNO GANCHO F.G 5/8 X 12\" C/T',1,0.00,1,'2026-01-22 14:51:37'),(1068,'C04439','PLANCHA GANCHO DE F.G. 1/2',1,0.00,1,'2026-01-22 14:51:37'),(1069,'C04440','GRAMPA DOBLE VIA',1,0.00,1,'2026-01-22 14:51:37'),(1070,'C04441','CONECTOR DOBLE PERNO AL 25MM-70MM',1,0.00,1,'2026-01-22 14:51:37'),(1071,'C04442','GRAPA SUSPENSIÓN MANITO AL',1,0.00,1,'2026-01-22 14:51:37'),(1072,'C04443','VANO DE 2 M X 0.5 M CON MARCO DE ANGULO DE 1\" x 1 1/2\" x 1/8\" + MALLA COCADA DE 2.5 CM x 2.5 CM + MARCO GALVANIZADO ARMABLE INTERIOR CON PLATINA DE 1 1/2\" x 1/8\"',5,0.00,1,'2026-01-22 14:51:37'),(1073,'C04444','SOPORTE RF 3M X 2.5° CON BRAZOS REGULABLES HASTA 1M',5,0.00,1,'2026-01-22 14:51:37'),(1074,'C04445','ESPARRAGO GALVANIZADO DE 1/2 X 40CM CON TUERCA Y CONTRA TUERCA',1,0.00,1,'2026-01-22 14:51:37'),(1075,'C04446','PERNO GALVANIZADOA DE 1/2 X 3° CON TUERCA Y CONTRA TUERCA',1,0.00,1,'2026-01-22 14:51:37'),(1076,'C04447','CABLE CAAI AUTOPORTANTE 2X16+ 16 MM',2,0.00,1,'2026-01-22 14:51:37'),(1077,'C04448','TABLERO MDP 3F+N-380/220VAC-60HZ TETRAPOLAR ROOFTOP',1,0.00,1,'2026-01-22 14:51:37'),(1078,'C04449','TABLERO PDP 3F+N-380/220VAC-60HZ TETRAPOLAR ROOFTOP',1,0.00,1,'2026-01-22 14:51:37'),(1079,'C04450','TABLERO PDP 3F+N+T 380/200VAC-60HZ TETRAPOLAR GREENFIED',1,0.00,1,'2026-01-22 14:51:37'),(1080,'C04451','TABLERO NAT PDP MON 9.9KKW 2F-220VAC-60HZ',1,0.00,1,'2026-01-22 14:51:37'),(1081,'C04452','SOPORTE RF 4M X 2.5\" PARA ROLDANA',5,0.00,1,'2026-01-22 14:51:37'),(1082,'C04453','SOPORTE RF 3M X 2.5° CON BRAZOS REGULABLES DE 20 A 40 CM',5,0.00,1,'2026-01-22 14:51:37'),(1083,'C04454','CABLE N2XOH 1X16 MM2 0,6/1 KV NEGRO',2,0.00,1,'2026-01-22 14:51:37'),(1084,'C04455','CABLE CAAI AUTOPORTANTE 2X25 + 16MM',2,0.00,1,'2026-01-22 14:51:37'),(1085,'C04456','SOPORTE RF 1M X 3\" CON BRAZOS PATA DE GALLO REGULABLES DE 40CM',5,0.00,1,'2026-01-22 14:51:37'),(1086,'C04457','SOPORTE RF 1M X 3\" CON BRAZOS DE 40CM CON DIAMETRO DE 50CM',5,0.00,1,'2026-01-22 14:51:37'),(1087,'C04458','SOPORTE RF 1M X 3\" CON BRAZOS DE 50CM A TUBO DE 3\"',5,0.00,1,'2026-01-22 14:51:37'),(1088,'C04459','SOPORTE RF 1M X 3\" CON BRAZOS DE 40CM CON DIAMETRO DE 20CM',5,0.00,1,'2026-01-22 14:51:37'),(1089,'C04460','SOPORTE RF 3.5M X 3\" CON BRAZOS REGULABLES DE 15 A 25CM A MASTIL DE 4\"',5,0.00,1,'2026-01-22 14:51:37'),(1090,'C04461','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES DE 45CM A 70CM A ROSETA',5,0.00,1,'2026-01-22 14:51:37'),(1091,'C04462','SOPORTE RF 3M X 2.5\" CON BRAZOS FIJOS DE 30CM A ROLDANA',5,0.00,1,'2026-01-22 14:51:37'),(1092,'C04463','SOPORTE RF 4M X 2.5\" CON BRAZOS REGULABLES DE 30 A 45 CM TUBO A TUBO',5,0.00,1,'2026-01-22 14:51:37'),(1093,'C04464','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES DE 30 A 45 CM A ROLDANA',5,0.00,1,'2026-01-22 14:51:37'),(1094,'C04465','TRANSFORMADOR COMBINADO DE TENSION Y CORRIENTE MONOFÁSICO',1,0.00,1,'2026-01-22 14:51:37'),(1095,'C04466','SOPORTE RF 2.5M X 3\" CON BRAZOS REGULABLES DE 30 A 45CM TUBO A TUBO',5,0.00,1,'2026-01-22 14:51:37'),(1096,'C04467','SOPORTE RF 1M X 2.5\" CON BRAZOS REGULABLES DE 40 A 70CM A TUBO',5,0.00,1,'2026-01-22 14:51:37'),(1097,'C04468','SOPORTE P/LUMINARIA 1.5M X 2\" X 3MM',5,0.00,1,'2026-01-22 14:51:37'),(1098,'C04469','ESCALERA PEATONAL DE 6M CON TUBO CUADRADO 2\" x 2\" CON ABRAZADERAS DE PL 1/4\"',5,0.00,1,'2026-01-22 14:51:37'),(1099,'C04470','TABLERO GENERAL 3F 220V 60HZ DE 600X500X170MM',1,0.00,1,'2026-01-22 14:51:37'),(1100,'C04471','SOPORTE RF 3M X 2.5\" CON BRAZOS DE 15CM + 80 CM INCLINADO CON SEPARACION DE 95CM',5,0.00,1,'2026-01-22 14:51:37'),(1101,'C04472','MASTIL RF 3M X 2\" X 2.5MM',5,0.00,1,'2026-01-22 14:51:37'),(1102,'C04473','ABRAZADERA P/POSTE DE DIAMETRO 42CM',5,0.00,1,'2026-01-22 14:51:37'),(1103,'C04474','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES DE 35 A 55CM TUBO A TUBO',5,0.00,1,'2026-01-22 14:51:37'),(1104,'C04475','SOPORTE RF 3.5M X 2.5\" CON BRAZOS DE 10CM A ROLDANA C/PLANCHA DE 15X15CM',5,0.00,1,'2026-01-22 14:51:37'),(1105,'C04476','SOPORTE MW 1M X 3\" CON BRAZOS REGULABLES DE 30 A 60 CM',5,0.00,1,'2026-01-22 14:51:37'),(1106,'C04477','POSTE CAC 11/350',1,0.00,1,'2026-01-22 14:51:37'),(1107,'C04478','CABLE CAAI 3X35+1XP25MM2',1,0.00,1,'2026-01-22 14:51:37'),(1108,'C04479','CABLE CAAI 1X35 MM2',1,0.00,1,'2026-01-22 14:51:37'),(1109,'C04480','SOPORTE TABLERO GALVANIZADO DE 1.8M X 0.45M CON TUBO CUADRADO DE 2 X 2\"',1,0.00,1,'2026-01-22 14:51:37'),(1110,'C04481','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES DE 35CM A 55CM A ROSETA',5,0.00,1,'2026-01-22 14:51:37'),(1111,'C04482','SOPORTE RF 3M X 2.5\" T/SHAKIRA CON BRAZOS DE 40 CM P/MONOPOLO',5,0.00,1,'2026-01-22 14:51:37'),(1112,'C04483','SOPORTE RF 3M X 2.5\" CON PLANCHA DE 20CM X 20CM P/ROLDANA',5,0.00,1,'2026-01-22 14:51:37'),(1113,'C04484','SOPORTE MW 1M X 3\" CON BRAZOS FIJOS DE 40 CM',5,0.00,1,'2026-01-22 14:51:37'),(1114,'C04485','SOPORTE RF 3M X 2.5\" PARA RONDANA CON ABACONES',5,0.00,1,'2026-01-22 14:51:37'),(1115,'C04486','CABLE CAAI 3X25 + N 25MM2 NEGRO',2,0.00,1,'2026-01-22 14:51:37'),(1116,'C04487','CABLE N2XOH 3-1X35MM2 NEGRO',2,0.00,1,'2026-01-22 14:51:37'),(1117,'C04488','CABLE THW-90 10 MM2 NEGRO',2,0.00,1,'2026-01-22 14:51:37'),(1118,'C04489','CABLE THW-90 16MM2 NEGRO',2,0.00,1,'2026-01-22 14:51:37'),(1119,'C04490','CURVA PVC SAP 3/4\"',1,0.00,1,'2026-01-22 14:51:37'),(1120,'C04491','ITM CAJA MOLDEADA TRIPOLAR 63A',1,0.00,1,'2026-01-22 14:51:37'),(1121,'C04492','TERMINAL T/CAÑON LARGO 16-8',1,0.00,1,'2026-01-22 14:51:37'),(1122,'C04493','TERMINAL TUBULAR P/CABLE 10MM',1,0.00,1,'2026-01-22 14:51:37'),(1123,'C04494','TUBO PVS SAP 3/4\" X 3MT',1,0.00,1,'2026-01-22 14:51:37'),(1124,'C04495','SOPORTE RF 3M X 2.5\" CON BRAZOS DE 40 CM A ROLDANA',5,0.00,1,'2026-01-22 14:51:37'),(1125,'C04496','FOTOCOPIADORA MULTIFUNCIONAL LASER RICOH MPC5503',1,0.00,1,'2026-01-22 14:51:37'),(1126,'C04497','TONER DE COLOR',1,0.00,1,'2026-01-22 14:51:37'),(1127,'C04498','CABLE N2XY 3-1x25MM² RETFLEX 0,6/1 KV CL-5',2,0.00,1,'2026-01-22 14:51:37'),(1128,'C04499','CINTA AISLANTE NEGRA',1,0.00,1,'2026-01-22 14:51:37'),(1129,'C04500','GABINETE METATLICO IP67 HERMETICO 400X300X150MM',1,0.00,1,'2026-01-22 14:51:37'),(1130,'C04501','ITM REGEGULABLE TRIPOLAR 70-100A',1,0.00,1,'2026-01-22 14:51:37'),(1131,'C04502','TERMINAL DE COMPRESION 16-6',1,0.00,1,'2026-01-22 14:51:37'),(1132,'C04503','TERMINAL DE COMPRESION 25-6',1,0.00,1,'2026-01-22 14:51:37'),(1133,'C04504','TUBO PVC SAP 1\"',1,0.00,1,'2026-01-22 14:51:37'),(1134,'C04505','POSTE CAC 15/500/225/450',1,0.00,1,'2026-01-22 14:51:37'),(1135,'C04506','TUBO PVC SAP 3\"X3MTS',1,0.00,1,'2026-01-22 14:51:37'),(1136,'C04507','CABLE P/COLOCAR ENERGIA ELECTRICA A TABLERO',2,0.00,1,'2026-01-22 14:51:37'),(1137,'C04508','CURVA PVC SAP 3\"',1,0.00,1,'2026-01-22 14:51:37'),(1138,'C04509','CAJA DE PASO 8X8X4 METALICO',1,0.00,1,'2026-01-22 14:51:37'),(1139,'C04510','LS GALVANIZADA DE 20 x 5CM',5,0.00,1,'2026-01-22 14:51:37'),(1140,'C04511','CINTILLO CV250 BLANCO',6,0.00,1,'2026-01-22 14:51:37'),(1141,'C04512','TABLERO DE PROTECCION RAYVOSS 3F/3H+N+T 380/220 VAC - 60HZ',1,0.00,1,'2026-01-22 14:51:37'),(1142,'C04513','CABLE N2XOH 1X6MM NEGRO',2,0.00,1,'2026-01-22 14:51:37'),(1143,'C04514','CONMUTADOR 63A 3 POLOS 1-0-2',1,0.00,1,'2026-01-22 14:51:37'),(1144,'C04515','VANO DE 1.2 M X 0.8 M CON MARCO DE ANGULO DE 1\" x 1 1/2\" x 1/8\" + MALLA COCADA DE 2.5 CM x 2.5 CM + MARCO GALVANIZADO ARMABLE INTERIOR CON PLATINA DE 1 1/2\" x 1/8\"',5,0.00,1,'2026-01-22 14:51:37'),(1145,'C04516','MANGA TERMOCONTRAIBLE 25MM AZUL',2,0.00,1,'2026-01-22 14:51:37'),(1146,'C04518','RIEL DIN P/TABLERO',2,0.00,1,'2026-01-22 14:51:37'),(1147,'C04519','PAR DE BRAZOS TIPO SHAKIRA CON BRAZOS DE 70CM',5,0.00,1,'2026-01-22 14:51:37'),(1148,'C04520','TRANSFORMADOR DE DISTRIBUCIÓN MONOFÁSICO DE 25 KVA FASE-FASE',1,0.00,1,'2026-01-22 14:51:37'),(1149,'C04521','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES TIPO PATA DE GALLO',5,0.00,1,'2026-01-22 14:51:37'),(1150,'C04522','SOPORTE RF 3M X 2.5\" DE TUBO A TUBO DE 2.5\" A 2\"',5,0.00,1,'2026-01-22 14:51:37'),(1151,'C04523','SOPORTE RF 3M X 2.5\" PARA RONDANA CON PLANCHA',5,0.00,1,'2026-01-22 14:51:37'),(1152,'C04524','SOPORTE RF 3M X 2.5\" T/SHAKIRA CON BRAZOS DE 70 CM',5,0.00,1,'2026-01-22 14:51:37'),(1153,'C04525','ANGULO L 2\"X2\"X3/16\" DE 40CM',1,0.00,1,'2026-01-22 14:51:37'),(1154,'C04526','ARRIOSTRE DE 2.5\"X4.28M CON SU CONTRA ARRIOSTRE 2\"X0.94M',5,0.00,1,'2026-01-22 14:51:37'),(1155,'C04527','MASTIL 6M X 4\" EN 2 PARTES CON BRIDA PARA UNION',5,0.00,1,'2026-01-22 14:51:37'),(1156,'C04528','PASOS 1/2\"',5,0.00,1,'2026-01-22 14:51:37'),(1157,'C04529','ROSETA DE 26CM DE DIAMETRO',5,0.00,1,'2026-01-22 14:51:37'),(1158,'C04530','PAR DE PLANCHA DE 20CM X 20CM CON ABOLT 2\" Y UBOL DE 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1159,'C04531','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES DE 30CM A 60CM A ROLDANA',5,0.00,1,'2026-01-22 14:51:37'),(1160,'C04532','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES DE 30CM A 70CM A ROSETA',5,0.00,1,'2026-01-22 14:51:37'),(1161,'C04533','ESCALERILLA GALV. DE 31CM',2,0.00,1,'2026-01-22 14:51:37'),(1162,'C04534','LS GALV. DE 12CM X 2\"',5,0.00,1,'2026-01-22 14:51:37'),(1163,'C04535','ESCUADRA GALV. DE 90° DE 5CM X 5CM X 2\"',5,0.00,1,'2026-01-22 14:51:37'),(1164,'C04536','PAR DE ROSETA RF CON DIAMETRO 21CM',5,0.00,1,'2026-01-22 14:51:37'),(1165,'C04537','SOPORTE RF 3M X 2\" CON BRAZOS DE 30CM PARA POSTE',5,0.00,1,'2026-01-22 14:51:37'),(1166,'C04538','PAR DE BRAZO REGULABLE MAYOR A 45 CM P/MASTIL 2.5\" A 4\"',5,0.00,1,'2026-01-22 14:51:37'),(1167,'C04539','ABRAZADERA TIPO STRUT EMT 2\'\'',1,0.00,1,'2026-01-22 14:51:37'),(1168,'C04540','BREAKER TRIPOLAR 100 AMP',1,0.00,1,'2026-01-22 14:51:37'),(1169,'C04541','CAJA DE PASO METALICA 10 X 10 X 4 CIEGA 1/2',1,0.00,1,'2026-01-22 14:51:37'),(1170,'C04542','CANAL TIPO STRUT RANURADO 41*21*1.2MM * 2.4MTS',1,0.00,1,'2026-01-22 14:51:37'),(1171,'C04543','ESPARRAGO 1/2\'\' X 1.80MTS',1,0.00,1,'2026-01-22 14:51:37'),(1172,'C04544','TABLERO TRIFASICO 3X100',1,0.00,1,'2026-01-22 14:51:37'),(1173,'C04545','TACO DE EXPANSION TIPO HILTI 1/2\'\'',1,0.00,1,'2026-01-22 14:51:37'),(1174,'C04546','SOPORTE RF 3M X 2.5\" CON BRAZOS DE 40CM A POSTE',5,0.00,1,'2026-01-22 14:51:37'),(1175,'C04547','SOPORTE RF 3M X 2.5\" CON BRAZOS CURVA DE 70CM REGULABLE PATA DE GALLO',5,0.00,1,'2026-01-22 14:51:37'),(1176,'C04548','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES P/ROLDANA DE 2\"',5,0.00,1,'2026-01-22 14:51:37'),(1177,'C04549','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES P/PIVOTANTE DE ANGULO DE 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1178,'C04550','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES P/PIVOTANTE DE CANAL C 4\"',5,0.00,1,'2026-01-22 14:51:37'),(1179,'C04551','SOPORTE RF 3M X 2.5\" CON BRAZOS TUBO A TUBO DE 70CM',5,0.00,1,'2026-01-22 14:51:37'),(1180,'C04552','SOPORTE RF 3M X 2.5\" CON PLANCHA DE 20CM x 20CM',5,0.00,1,'2026-01-22 14:51:37'),(1181,'C04553','SOPORTE RF 3M X 2.5\" P/PIVOTANTE DE ANGULO 2\" A 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1182,'C04554','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES DE 20CM A 40CM A ROSETA',5,0.00,1,'2026-01-22 14:51:37'),(1183,'C04555','SOPORTE MW 1.5M X 4° T/BURRITO CON BRAZOS LARGUEROS REGULABLES DE 1M',5,0.00,1,'2026-01-22 14:51:37'),(1184,'C04556','SOPORTE RF 3M X 2.5° CON BRAZOS DE 40 CM T/PATA DE GALLO',5,0.00,1,'2026-01-22 14:51:37'),(1185,'C04557','GABINETE 42RU 2.06 X 0.8 X 1 MT',1,0.00,1,'2026-01-22 14:51:37'),(1186,'C04558','TERMINAL T/OJO 35-8',1,0.00,1,'2026-01-22 14:51:37'),(1187,'C04559','TABLERO INTEGRADO 3F+N+T 380/220VAC-60HZ',1,0.00,1,'2026-01-22 14:51:37'),(1188,'C04560','SOPORTE RF 3M X 2.5\" CON BRAZOS DE 70CM CON ABRAZADERA INVERTIDA DE 2\" A 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1189,'C04561','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES DE 40 A 60CM INVERTIDOS P/ANGULO DE 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1190,'C04562','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES TUBO A TUBO DE 2.5\" A 4\"',5,0.00,1,'2026-01-22 14:51:37'),(1191,'C04563','SOPORTE RF 3M X 2.5\" CON BRAZOS TUBO A TUBO DE 70CM DE 2.5\" A 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1192,'C04564','SOPORTE RF 3M X 2.5\" CON BRAZOS CURVA DE 70CM',5,0.00,1,'2026-01-22 14:51:37'),(1193,'C04565','SOPORTE RF 3M X 2.5\" CON BRAZOS INVERTIDOS REGULABLES DE 2\" A 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1194,'C04566','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES DE 1.5\" A 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1195,'C04567','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES DE ANGULO DE 2.5\" A TUBO DE 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1196,'C04568','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES DE TUBO A TUBO DE 2.5\" A 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1197,'C04569','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES INVERTDOS DE VIGA TIPO C A TUBO DE 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1198,'C04570','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES PARA ROLDANA TUBULAR',5,0.00,1,'2026-01-22 14:51:37'),(1199,'C04571','SOPORTE RF 3M X 2.5\" CON PLANCHA DE 20X20CM PARA ROLDANA TUBULAR',5,0.00,1,'2026-01-22 14:51:37'),(1200,'C04572','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES A ANGULO DE 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1201,'C04573','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULALES TUBO A TUBO PARA ROLDANA',5,0.00,1,'2026-01-22 14:51:37'),(1202,'C04574','SOPORTE RF 3M X 2.5\" CON PLANCHA A ANGULO DE 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1203,'C04575','SOPORTE RF 3M X 2.5\" T/PATA DE GALLO CON CURVA A LA DERECHA',5,0.00,1,'2026-01-22 14:51:37'),(1204,'C04576','SOPORTE RF 3M X 2.5\" T/SHAKIRA CON BRAZOS REGULABLES',5,0.00,1,'2026-01-22 14:51:37'),(1205,'C04578','SOPORTE RF 3M X 2.5\" CON PLANCHAS P/PIVOTANTE A ANGULO',5,0.00,1,'2026-01-22 14:51:37'),(1206,'C04577','ABRAZADERA ESTABILIZADORA CON DIAMETRO 19CM',5,0.00,1,'2026-01-22 14:51:37'),(1207,'C04579','ABRAZADERA T/CRUCETA ESTABILIZADORA CON DIAMETRO 20CM CON TUBO CUADRADO',5,0.00,1,'2026-01-22 14:51:37'),(1208,'C04580','CABLE DE ACERO TIPO RETENIDA 3/8',2,0.00,1,'2026-01-22 14:51:37'),(1209,'C04581','CANDADO TEMPLADOR DE CABLE 3/8\"',5,0.00,1,'2026-01-22 14:51:37'),(1210,'C04582','VARILLA GALVANIZADA DE 5/8\" DE UN OJO',1,0.00,1,'2026-01-22 14:51:37'),(1211,'C04583','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES P/ROSETA CON OJO CHINO',5,0.00,1,'2026-01-22 14:51:37'),(1212,'C04584','SOPORTE RF 3M X 2.5\" CON BRAZOS INVERTIDOS REGULABLES DE 2.5\" A 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1213,'C04585','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES 1.5\" A TUBO DE 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1214,'C04586','SOPORTE RF 3M x 2.5\" CON BRAZOS REGULABLES INVERTIDOS DE ANGULO 2.5\" A TUBO 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1215,'C04587','SOPORTE RF 3M X 2.5\" CON PAR PLANCHAS PARA ANGULO DE 2.5\" A TUBO DE 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1216,'C04588','PANEL LED EMPOTRADO LEDVANCE 600X600 36W 6500K',1,0.00,1,'2026-01-22 14:51:37'),(1217,'C04589','LUMINARIA T/DOWNLIGHT CIRCULAR 18W 1500ML 6500K',1,0.00,1,'2026-01-22 14:51:37'),(1218,'C04590','CABLE 35MM P/BATERIA COLOR AZUL',2,0.00,1,'2026-01-22 14:51:37'),(1219,'C04591','CABLE 35MM P/BATERIA COLOR NEGRO',2,0.00,1,'2026-01-22 14:51:37'),(1220,'C04592','CABLE 35MM P/BATERIA COLOR ROJO',2,0.00,1,'2026-01-22 14:51:37'),(1221,'C04593','CABLE 35MM P/BATERIA COLOR VERDE/AMARILLO',2,0.00,1,'2026-01-22 14:51:37'),(1222,'C04594','TABLERO DE PROTECCION RAYVOSS 2F/2H + T 220 VAC - 60HZ',1,0.00,1,'2026-01-22 14:51:37'),(1223,'C04595','TABLERO DE PROTECCION RAYVOSS 2F/2H + T 220 VAC - 60HZ',1,0.00,1,'2026-01-22 14:51:37'),(1224,'C04596','CABLE CAAI 2X35+P25MM2',2,0.00,1,'2026-01-22 14:51:37'),(1225,'C04597','SOPORTE RF 1M X 3\" CON BRAZOS REGULABLES DE TUBO A TUBO DE 1.5\" A 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1226,'C04598','CABLE NH-90 450/750 V 16MM2 NEGRO',2,0.00,1,'2026-01-22 14:51:37'),(1227,'C04599','CAJA DE PASO METALICA 300X300X150MM',1,0.00,1,'2026-01-22 14:51:37'),(1228,'C04600','CONECTOR BIMETALICO CU-AL 25-16 MM2',1,0.00,1,'2026-01-22 14:51:37'),(1229,'C04601','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES TUBO A TUBO DE 3.5\" A 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1230,'C04602','SOPORTE RF 3M X 2.5\" CON PLANCHAS P/ROLDANA',5,0.00,1,'2026-01-22 14:51:37'),(1231,'C04603','SOPORTE RF 2M x 2.5\" CON BRAZOS DE 40CM P/POSTE',5,0.00,1,'2026-01-22 14:51:37'),(1232,'C04604','SOPORTE RF 3M X 2.5\" CON PLANCHA INVERTIDA INCLUYE ABOL Y UBOL EN PIVOTANTE CON ÁNGULO 2\"',5,0.00,1,'2026-01-22 14:51:37'),(1233,'C04605','SLIM PLAFON LED ADOSAR REDONDO BLANCO 24W/865',1,0.00,1,'2026-01-22 14:51:37'),(1234,'C04606','BANDEJA TIPO PERFORADA O RANURADA EN PLANCHA (1MM) DE 300 X 100 X 2,40 MTRS COLOR AMARILLO',1,0.00,1,'2026-01-22 14:51:37'),(1235,'C04607','RIEL STRUT BAJO 41X21X2,4 MTRS EN 9,12MM',1,0.00,1,'2026-01-22 14:51:37'),(1236,'C04608','TEE DE BANDEJA TIPO PERFORADA O RANURADA EN PLANCHA 1/16 (1,5MM) DE 200X300X100 SIN TAPA COLOR AMARILLO',1,0.00,1,'2026-01-22 14:51:37'),(1237,'C04609','TUERCA HEXAGONAL 3/8\"',1,0.00,1,'2026-01-22 14:51:37'),(1238,'C04610','VARILL ROSCADA DE 3/8 X 1,80 MTRS',1,0.00,1,'2026-01-22 14:51:37'),(1239,'C04611','SOPORTE RF 1M X 2.5\" CON BRAZOS DE 40CM P/POSTE',5,0.00,1,'2026-01-22 14:51:37'),(1240,'C04612','SOPORTE RF 1M X 2.5\" CON BRAZOS DE 30CM P/ROSETA CON OJO CHINO',5,0.00,1,'2026-01-22 14:51:37'),(1241,'C04613','SOPORTE P/GABINETE 1.20 M',5,0.00,1,'2026-01-22 14:51:37'),(1242,'C04614','PUERTA METALICA 1X0.9M',5,0.00,1,'2026-01-22 14:51:37'),(1243,'C04615','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES CON CURVA',5,0.00,1,'2026-01-22 14:51:37'),(1244,'C04616','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES P/ROSETA',5,0.00,1,'2026-01-22 14:51:37'),(1245,'C04617','SOPORTE RF 3M X 2.5\" P/PIVOTANTE DE ANGULO 2.5\" A TUBO DE 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1246,'C04618','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES INVERTIDOS PARA ROLDANA DE TUBO A TUBO',5,0.00,1,'2026-01-22 14:51:37'),(1247,'C04619','SOPORTE RF 3M X 2.5\" CON BRAZOS DE 15CM DE 2.5\" A TUBO DE 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1248,'C04620','SOPORTE RF 3M X 2.5\" CON BRAZOS PARA ROSETA DE 30CM',5,0.00,1,'2026-01-22 14:51:37'),(1249,'C04621','SOPORTE RF 3M X 2.5\" CON BRAZOS DE 35CM A POSTE',5,0.00,1,'2026-01-22 14:51:37'),(1250,'C04622','SOPORTE RF 3M X 2.5\" CON BRAZOS REGULABLES P/PIVOTANTE DE CANAL C A TUBO DE 2.5\"',5,0.00,1,'2026-01-22 14:51:37'),(1251,'C04623','SOPORTE RF 3M X 2.5\" CON PLANCHAS CON UBOLES DE 2.5\" INVERTIDO P/PIVOTANTE',5,0.00,1,'2026-01-22 14:51:37'),(1252,'C04624','SOPORTE RF 3M X 2.5\" CON PLANCHAS CON UBOLES DE 4\" A UBOLES 2.5\" INVERTIDO P/PIVOTANTE',5,0.00,1,'2026-01-22 14:51:37'),(1253,'C04625','BASE T/ABRAZADERA 0.40CM CON DIAMETROS 16.50 CM PARTE SUPERIOR, 17.50CM INFERIOR Y BASE 20CM',5,0.00,1,'2026-01-22 14:51:37'),(1254,'C04626','STRETCH FILM TRANSPARENTE 18\" X 2.0 KG X 20 MICRAS CAJA X 4 UNID',1,0.00,1,'2026-01-22 14:51:37'),(1255,'C04627','CINTA DE EMBALAJE TRANSPARENTE CAJA X 36 UNID',1,0.00,1,'2026-01-22 14:51:37'),(1256,'C04628','CINTILLO METALICO 20CM',6,0.00,1,'2026-01-22 14:51:37'),(1257,'C04629','CRUCETA PARA RESERVA DE 60CM',1,0.00,1,'2026-01-22 14:51:37'),(1258,'C04630','TUBO PVC SAP 2\"X 3MTS',1,0.00,1,'2026-01-22 14:51:37'),(1259,'C04631','CONECTOR P-25 BIMETALICO ALUMINIO COBRE 16-95mm 2 4-25mm2',1,0.00,1,'2026-01-22 14:51:37'),(1260,'C04632','TUBO PVC SAP 2\"X 3MTS',1,0.00,1,'2026-01-22 14:51:37'),(1261,'C04633','SOPORTE P/LUMINARIA 1.5M X 1 1/2\" X 2MM',5,0.00,1,'2026-01-22 14:51:37');


-- Nivel 1
INSERT INTO aprobador (id_trabajador, id_cliente, id_area, nivel, activo) VALUES
                                                                              (32, 17, 3, 1, 1),  -- ENTEL PERU S.A, ENERGIA
                                                                              (32, 17, 13, 1, 1), -- ENTEL PERU S.A, TI
                                                                              (29, 2, 6, 1, 1),   -- AMERICA MOVIL, PEXT
                                                                              (29, 9, 6, 1, 1),   -- GYGA CONSULTING, PEXT
                                                                              (29, 14, 6, 1, 1),  -- SOLUCIONES TEC, PEXT
                                                                              (48, 14, 13, 1, 1), -- SOLUCIONES TEC, TI
                                                                              (48, 2, 13, 1, 1),  -- AMERICA MOVIL, TI
                                                                              (39, 16, 16, 1, 1), -- COMFUTURA, LOGÍSTICA
                                                                              (9, 2, 8, 1, 1),    -- AMERICA MOVIL, SAQ
                                                                              (9, 17, 8, 1, 1),   -- ENTEL PERU, SAQ
                                                                              (9, 14, 8, 1, 1),   -- SOLUCIONES TEC, SAQ
                                                                              (23, 2, 4, 1, 1),   -- AMERICA MOVIL, CW
                                                                              (23, 2, 3, 1, 1),   -- AMERICA MOVIL, ENERGIA
                                                                              (23, 17, 4, 1, 1),  -- ENTEL PERU, CW
                                                                              (23, 14, 4, 1, 1),  -- SOLUCIONES TEC, CW
                                                                              (10, 17, 6, 1, 1);  -- ENTEL PERU, PEXT
-- Nivel 2
INSERT INTO aprobador (id_trabajador, id_cliente, id_area, nivel, activo) VALUES
                                                                              (2, 17, 3, 2, 1),  -- ENTEL PERU S.A, ENERGIA
                                                                              (2, 17, 13, 2, 1), -- ENTEL PERU S.A, TI
                                                                              (2, 2, 6, 2, 1),   -- AMERICA MOVIL, PEXT
                                                                              (2, 9, 6, 2, 1),   -- GYGA CONSULTING, PEXT
                                                                              (2, 14, 6, 2, 1),  -- SOLUCIONES TEC, PEXT
                                                                              (34, 14, 13, 2, 1),-- SOLUCIONES TEC, TI
                                                                              (34, 2, 13, 2, 1), -- AMERICA MOVIL, TI
                                                                              (34, 16, 16, 2, 1),-- COMFUTURA, LOGÍSTICA
                                                                              (34, 2, 8, 2, 1),  -- AMERICA MOVIL, SAQ
                                                                              (34, 17, 8, 2, 1), -- ENTEL PERU, SAQ
                                                                              (34, 14, 8, 2, 1), -- SOLUCIONES TEC, SAQ
                                                                              (34, 2, 4, 2, 1),  -- AMERICA MOVIL, CW
                                                                              (34, 2, 3, 2, 1),  -- AMERICA MOVIL, ENERGIA
                                                                              (34, 17, 4, 2, 1), -- ENTEL PERU, CW
                                                                              (34, 14, 4, 2, 1), -- SOLUCIONES TEC, CW
                                                                              (34, 17, 6, 2, 1); -- ENTEL PERU, PEXT
-- Nivel 3
INSERT INTO aprobador (id_trabajador, id_cliente, id_area, nivel, activo) VALUES
                                                                              (28, 17, 3, 3, 1),  -- ENTEL PERU S.A, ENERGIA
                                                                              (28, 17, 13, 3, 1), -- ENTEL PERU S.A, TI
                                                                              (28, 2, 6, 3, 1),   -- AMERICA MOVIL, PEXT
                                                                              (28, 9, 6, 3, 1),   -- GYGA CONSULTING, PEXT
                                                                              (28, 14, 6, 3, 1),  -- SOLUCIONES TEC, PEXT
                                                                              (28, 14, 13, 3, 1), -- SOLUCIONES TEC, TI
                                                                              (28, 2, 13, 3, 1),  -- AMERICA MOVIL, TI
                                                                              (28, 16, 16, 3, 1), -- COMFUTURA, LOGÍSTICA
                                                                              (28, 2, 8, 3, 1),   -- AMERICA MOVIL, SAQ
                                                                              (28, 17, 8, 3, 1),  -- ENTEL PERU, SAQ
                                                                              (28, 14, 8, 3, 1),  -- SOLUCIONES TEC, SAQ
                                                                              (28, 2, 4, 3, 1),   -- AMERICA MOVIL, CW
                                                                              (28, 2, 3, 3, 1),   -- AMERICA MOVIL, ENERGIA
                                                                              (28, 17, 4, 3, 1),  -- ENTEL PERU, CW
                                                                              (28, 14, 4, 3, 1),  -- SOLUCIONES TEC, CW
                                                                              (28, 17, 6, 3, 1);  -- ENTEL PERU, PEXT
INSERT INTO tipo_ot (codigo, descripcion, activo) VALUES
                                                      ('Operativa',  'operaciones de trabajo', 1),
                                                      ('Administrativa',  'administracion del trabajo', 1);

