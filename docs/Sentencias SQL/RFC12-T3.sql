SELECT A.SEMANA, 'Mayor Solicitudes' AS CATEGORIA, A.IDOPERADOR, SOLICITUDES FROM

(SELECT IDOPERADOR, TO_NUMBER(TO_CHAR(FECHA,'WW')) AS Semana, COUNT(IDRESERVA) as SOLICITUDES
FROM ALOJAMIENTO NATURAL JOIN RESERVASDEALOJAMIENTO NATURAL JOIN RESERVA CROSS JOIN
(SELECT *
FROM LISTA_DE_FECHAS
WHERE FECHA BETWEEN TO_DATE('01/12/2019','DD/MM/YYYY') AND TO_DATE('31/12/2019','DD/MM/YYYY'))
WHERE FECHA BETWEEN INICIOESTADIA AND FINESTADIA
GROUP BY IDOPERADOR, TO_NUMBER(TO_CHAR(FECHA,'WW'))) A

INNER JOIN

(SELECT SEMANA, MAX(SOLICITUDES) AS MAXIMO
FROM
(SELECT IDOPERADOR, TO_NUMBER(TO_CHAR(FECHA,'WW')) AS Semana, COUNT(IDRESERVA) as SOLICITUDES
FROM ALOJAMIENTO NATURAL JOIN RESERVASDEALOJAMIENTO NATURAL JOIN RESERVA CROSS JOIN
(SELECT *
FROM LISTA_DE_FECHAS
WHERE FECHA BETWEEN TO_DATE('01/12/2019','DD/MM/YYYY') AND TO_DATE('31/12/2019','DD/MM/YYYY'))
WHERE FECHA BETWEEN INICIOESTADIA AND FINESTADIA
GROUP BY IDOPERADOR, TO_NUMBER(TO_CHAR(FECHA,'WW')))
GROUP BY SEMANA) B

ON A.SOLICITUDES=B.MAXIMO AND A.SEMANA=B.SEMANA

;