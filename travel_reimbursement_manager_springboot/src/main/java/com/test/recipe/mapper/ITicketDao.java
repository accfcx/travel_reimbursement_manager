package com.test.recipe.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.test.recipe.dto.TicketResult;
import com.test.recipe.model.Ticket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ITicketDao {
    IPage<TicketResult> selectTicketsByTime(IPage<TicketResult> pages,
                                            @Param("startTime") String startTime,
                                            @Param("arrivalTime")String arrivalTime,
                                            @Param("empno") Integer empno);

    List<TicketResult> selectAllTickets(@Param("empno") Integer empno);

    boolean delTickets(@Param("tids") List<Integer> tids);

    boolean addTicket(Ticket ticket);

    boolean updateTravelId(@Param("ticket") TicketResult ticket);

    List<TicketResult> selectTicketsByTravelId(@Param("travelId") Integer travelId);

    boolean disassociateTickets(@Param("travelIds") List<Integer> travelIds);

    List<Integer> selectTidsWithoutAssociation();

    List<Integer> selectTidsByEmpnos(@Param("empnos") List<Integer> empnos);

    List<Integer> selectTravelIdsByTids(@Param("tids") List<Integer> tids);

    boolean updateTravelDesc(@Param("ticket") TicketResult ticket);

    List<Ticket> selectTicketsByTransId(@Param("transIds") List<Integer> transIds);
}
