package com.team1.efep;

import com.team1.efep.enums.Const;
import com.team1.efep.models.entity_models.*;
import com.team1.efep.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class EfepApplication {

    private final AccountRepo accountRepo;
    private final AccountStatusRepo accountStatusRepo;
    private final FeedbackRepo feedbackRepo;
    private final FeedbackStatusRepo feedbackStatusRepo;
    private final FlowerRepo flowerRepo;
    private final FlowerStatusRepo flowerStatusRepo;
    private final OrderRepo orderRepo;
    private final OrderStatusRepo orderStatusRepo;
    private final PurchasedPlanStatusRepo purchasedPlanStatusRepo;


    public static void main(String[] args) {
        SpringApplication.run(EfepApplication.class, args);
    }


    @Bean
    public CommandLineRunner initData() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {

                //init Account status
                AccountStatus activeAccount = accountStatusRepo.save(
                        AccountStatus.builder().status(Const.ACCOUNT_STATUS_ACTIVE).build()
                );

                AccountStatus bannedAccount = accountStatusRepo.save(
                        AccountStatus.builder().status(Const.ACCOUNT_STATUS_BANNED).build()
                );

                //init Feedback status
                FeedbackStatus repliedFeedback = feedbackStatusRepo.save(
                        FeedbackStatus.builder().status(Const.FEEDBACK_STATUS_REPLIED).build()
                );

                FeedbackStatus unRepliedFeedback = feedbackStatusRepo.save(
                        FeedbackStatus.builder().status(Const.FEEDBACK_STATUS_UN_REPLIED).build()
                );

                //init Flower status
                FlowerStatus availableFlower = flowerStatusRepo.save(
                        FlowerStatus.builder().status(Const.FLOWER_STATUS_AVAILABLE).build()
                );

                FlowerStatus outOfStockFlower = flowerStatusRepo.save(
                        FlowerStatus.builder().status(Const.FLOWER_STATUS_OUT_OF_STOCK).build()
                );

                FlowerStatus deletedFlower = flowerStatusRepo.save(
                        FlowerStatus.builder().status(Const.FLOWER_STATUS_DELETED).build()
                );

                //init Order status
                OrderStatus processingOrder = orderStatusRepo.save(
                        OrderStatus.builder().status(Const.ORDER_STATUS_PROCESSING).build()
                );

                OrderStatus packedOrder = orderStatusRepo.save(
                        OrderStatus.builder().status(Const.ORDER_STATUS_PACKED).build()
                );

                OrderStatus finishedOrder = orderStatusRepo.save(
                        OrderStatus.builder().status(Const.ORDER_STATUS_FINISHED).build()
                );

                OrderStatus canceledOrder = orderStatusRepo.save(
                        OrderStatus.builder().status(Const.ORDER_STATUS_CANCELLED).build()
                );

                //init purchased plan status

                PurchasedPlanStatus activePlan = purchasedPlanStatusRepo.save(
                        PurchasedPlanStatus.builder().status(Const.PURCHASED_PLAN_STATUS_ACTIVE).build()
                );

                PurchasedPlanStatus inactivePlan = purchasedPlanStatusRepo.save(
                        PurchasedPlanStatus.builder().status(Const.PURCHASED_PLAN_STATUS_INACTIVE).build()
                );

                PurchasedPlanStatus cancelledPlan = purchasedPlanStatusRepo.save(
                        PurchasedPlanStatus.builder().status(Const.PURCHASED_PLAN_STATUS_CANCELLED).build()
                );

            }
        };
    }

}
