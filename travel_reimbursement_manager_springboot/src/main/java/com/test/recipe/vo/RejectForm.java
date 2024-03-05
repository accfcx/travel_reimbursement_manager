package com.test.recipe.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName RejectForm
 * @Description TODO
 * @Author LiJian
 * @Date 2023/2/4
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RejectForm {
    private Integer rejectTravelId;
    private Integer uid;
    private String rejectionReason;
}
