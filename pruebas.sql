SELECT * from Participa;

UPDATE PARTICIPA SET posicion=3 WHERE DNI='01234567B' and NdeEdicion=0

SELECT dni_e FROM PARTICIPA  WHERE DNI='01234567B' and NdeEdicion=0
DELETE FROM PARTICIPA WHERE DNI='01234567B' AND NdeEdicion=0
INSERT INTO PARTICIPA VALUES ('01234567B', 0, 3, '23456789A')

create or replace TRIGGER
 pistaTresPartidos BEFORE INSERT OR UPDATE ON
 partido FOR EACH ROW
 DECLARE i int;
BEGIN
 select count(*) into i from partido where :new.fecha=fecha and :new.idpista=idpista;
 IF i = 3 THEN
      raise_application_error(-20000, ' Ya existen tres partidos asignados a esta pista el mismo dia');
    END IF;

END;

SELECT * FROM PARTIDO
INSERT INTO PARTIDO VALUES (000000000, TO_DATE('23/07/2000', 'DD/MM/YYYY'), 0, '12345678A', 12345)
UPDATE PARTIDO SET FECHA=TO_DATE('24/07/2000', 'DD/MM/YYYY') WHERE CODPARTIDO=323456789
