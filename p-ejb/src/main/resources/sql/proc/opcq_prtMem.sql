drop procedure if exists opcq_prtMem;

DELIMITER $$
#CALL opcq_prtMem('2');
CREATE PROCEDURE opcq_prtMem(
	in tranId longtext
)  
BEGIN
	drop temporary table if exists t_prtmem_mod;
	
	create temporary table t_prtmem_mod
	select a.id, a.beId, a.status, a.createUid as apvUid
	from opcqmem as a
	where find_in_set(a.id, tranId);

	-- Query 0 opcqmem
	select a.id as memId, a.*
    from opcqmem as a
    where a.id in (select x.id from t_prtmem_mod x)
	order by a.code;
   
	-- Query 1 opcqmemt
	select b.hId as memId, b.*, c.code as bookCode, c.`desc` as bookDesc 
	from opcqmemt as b, opcqbook as c
	where b.hId in (select x.id from t_prtmem_mod x) 
		and b.bookId = c.id 
	order by b.hId, b.itemNo;
    
	drop temporary table if exists t_prtmem_mod;
END$$
DELIMITER ;