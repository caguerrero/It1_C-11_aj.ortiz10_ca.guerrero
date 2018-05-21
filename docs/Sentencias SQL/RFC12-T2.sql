SELECT A.SEMANA, 'Menor Ocupacion' AS CATEGORIA, A.IDALOJAMIENTO, MAXIMO FROM

(SELECT TO_NUMBER(TO_CHAR(FECHA,'WW')) AS SEMANA, IDALOJAMIENTO,  AVG(OCUPACION) AS OCUPACION
FROM
(SELECT IDALOJAMIENTO, FECHA, COUNT(IDRESERVA)/CAPACIDAD as OCUPACION
FROM ALOJAMIENTO NATURAL JOIN RESERVASDEALOJAMIENTO NATURAL JOIN RESERVA CROSS JOIN
(SELECT FECHA
FROM LISTA_DE_FECHAS
WHERE FECHA BETWEEN TO_DATE('01/12/2019','DD/MM/YYYY') AND TO_DATE('31/12/2019','DD/MM/YYYY'))
WHERE FECHA BETWEEN INICIOESTADIA AND FINESTADIA
GROUP BY IDALOJAMIENTO, FECHA, CAPACIDAD)
GROUP BY IDALOJAMIENTO, TO_NUMBER(TO_CHAR(FECHA,'WW'))) A

INNER JOIN

(SELECT SEMANA, MIN(OCUPACION) AS MAXIMO
FROM
(SELECT TO_NUMBER(TO_CHAR(FECHA,'WW')) AS SEMANA, IDALOJAMIENTO,  AVG(OCUPACION) AS OCUPACION
FROM
(SELECT IDALOJAMIENTO, FECHA, COUNT(IDRESERVA)/CAPACIDAD as OCUPACION
FROM ALOJAMIENTO NATURAL JOIN RESERVASDEALOJAMIENTO NATURAL JOIN RESERVA CROSS JOIN
(SELECT FECHA
FROM LISTA_DE_FECHAS
WHERE FECHA BETWEEN TO_DATE('01/12/2019','DD/MM/YYYY') AND TO_DATE('31/12/2019','DD/MM/YYYY'))
WHERE FECHA BETWEEN INICIOESTADIA AND FINESTADIA
GROUP BY IDALOJAMIENTO, FECHA, CAPACIDAD)
GROUP BY IDALOJAMIENTO, TO_NUMBER(TO_CHAR(FECHA,'WW')))
GROUP BY SEMANA) B 

ON (A.OCUPACION=B.MAXIMO AND A.SEMANA=B.SEMANA);
