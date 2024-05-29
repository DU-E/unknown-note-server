package unknownnote.unknownnoteserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import unknownnote.unknownnoteserver.dto.EssayDTO;
import unknownnote.unknownnoteserver.entity.*;
import unknownnote.unknownnoteserver.repository.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;


@Service
public class EssayService {

    @Autowired
    private EssayRepository essayRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSubscribeRepository userSubscribeRepository;

    @Autowired
    private UserViewedEssaysRepository userViewedEssaysRepository;

    public Essay saveNewEssay(EssayDTO essayDTO, int userId) {
        try {
            int userid = userId;

            Essay essayEntity = new Essay();
            essayEntity.setETitle(essayDTO.getETitle());
            essayEntity.setEContent(essayDTO.getEContent());
            essayEntity.setOpenable(essayDTO.getOpenable());
            essayEntity.setEssayTime(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            essayEntity.setELikes(essayDTO.getELikes());

            String category = essayDTO.getECategory();
            if (category != null) {
                essayEntity.setECategory(category.toUpperCase());
            } else {
                essayEntity.setECategory("DEFAULT");
            }

            User userEntity = userRepository.findById(userid).orElseThrow(() -> new RuntimeException("User not found: " + userid));

            if (userEntity != null) {
                essayEntity.setUser(userEntity);
                return essayRepository.save(essayEntity);
            }

            return null;
        } catch (Exception e) {
           // e.printStackTrace(); //디버깅용 결과 모두 보여주기 위해
            throw new RuntimeException("Unexpected Error during saveNewEssay()", e);
        }
    }

    public Essay updateEssay(int essayId, String eContent, String eCategory, String eTitle, int openable, int userId) {
        Optional<Essay> essayOptional = essayRepository.findById(essayId);
        if (essayOptional.isPresent()) {
            Essay essayEntity = essayOptional.get();

            if (essayEntity.getUser().getUserId() == userId) {

                essayEntity.setEContent(eContent);
                essayEntity.setECategory(eCategory);
                essayEntity.setOpenable(openable);
                essayEntity.setETitle(eTitle);

                return essayRepository.save(essayEntity);
            } else {
                System.err.println("Userid not match during changing essay");
                return null;
            }
        } else {
            System.err.println("Requested essayid do not exists");
            return null;
        }
    }
    public Essay addLike(int essayId, int userId) {
        Optional<Essay> essayOptional = essayRepository.findById(essayId);
        if (essayOptional.isPresent()) {
            Essay essay = essayOptional.get();
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

            if (!user.getLikedEssays().contains(essay)) {
                user.getLikedEssays().add(essay);
                System.out.println("Adding like from essayId: " + essayId + " for userId: " + userId);
                essay.setELikes(essay.getELikes() + 1);
                userRepository.save(user);
                return essayRepository.save(essay);
            }
        }
        return null;
    }

    public boolean removeLike(int essayId, int userId) {
        Optional<Essay> essayOptional = essayRepository.findById(essayId);
        if (essayOptional.isPresent()) {
            Essay essay = essayOptional.get();
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getLikedEssays().remove(essay)) {
                    System.out.println("Removing like from essayId: " + essayId + " for userId: " + userId);
                    essay.setELikes(essay.getELikes() - 1);
                    essayRepository.saveAndFlush(essay);
                    userRepository.saveAndFlush(user);
                    return true;
                } else {
                    System.err.println("Essay not found in user's liked list");
                }
            } else {
                System.err.println("User not found");
            }
        } else {
            System.err.println("Essay not found");
        }
        return false;
    }


    public List<Essay> findAllLikedEssays(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return new ArrayList<>(user.getLikedEssays());
    }


    //카테고리순 나열 poem, novel, whisper있음
    public Page<Essay> findEssaysByCategory(String category, int page) {
        Pageable pageable = PageRequest.of(page, 20);
        return essayRepository.findByECategoryOrderByEssayTimeDesc(category, pageable);
    }

    public List<Essay> findAllEssaysBySubscribedUsers(int userId) {
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<UserSubscribe> subscriptions = userSubscribeRepository.findByUserId(userId);
        List<Essay> essays = new ArrayList<>();

        for (UserSubscribe subscription : subscriptions) {
            User subscribedUser = userRepository.findById(subscription.getFollowingId()).orElseThrow(() -> new RuntimeException("Subscribed user not found"));
            List<Essay> subscribedUserEssays = essayRepository.findByUser(subscribedUser);
            essays.addAll(subscribedUserEssays);
        }
        return essays;
    }

//    public Essay getRecommendedEssay(int userId) {
//        try {
//            // 사용자가 이미 본 에세이의 ID 목록 가져오기
//            List<Integer> viewedEssayIds = userViewedEssaysRepository.findViewedEssayIds(userId);
//
//            Essay recommendedEssay = null;
//            if (!viewedEssayIds.isEmpty()) {
//                List<Essay> unviewedEssays = essayRepository.findUnviewedEssays(viewedEssayIds);
//                if (!unviewedEssays.isEmpty()) {
//                    recommendedEssay = unviewedEssays.get(0); // 첫 번째 에세이를 선택
//                }
//            } else {
//                List<Essay> essays = essayRepository.findAnyEssay();
//                if (!essays.isEmpty()) {
//                    recommendedEssay = essays.get(0); // 첫 번째 에세이를 선택
//                }
//            }
//
//            // 추천된 에세이를 사용자가 본 에세이 목록에 추가
//            if (recommendedEssay != null) {
//                viewedEssayIds.add(recommendedEssay.getEssayId());
//
//                // 사용자가 이미 본 에세이를 UserViewedEssays 테이블에 기록
//                UserViewedEssaysEntity userViewedEssaysEntity = new UserViewedEssaysEntity();
//                UserViewedEssaysId userViewedEssaysId = new UserViewedEssaysId(userId, recommendedEssay.getEssayId());
//                userViewedEssaysEntity.setId(userViewedEssaysId);
//
//                // 생성된 UserViewedEssaysEntity를 저장한다.
//                userViewedEssaysRepository.save(userViewedEssaysEntity);
//
//                return recommendedEssay;
//            } else {
//                System.err.println("No unviewed essays found");
//                return null;
//            }
//        } catch (Exception e) {
//            // 예외 처리
//            System.err.println("An error occurred while getting recommended essay: " + e.getMessage());
//            throw new RuntimeException("error occurred while getting recommended essay", e);
//        }
//    }
    public List<Essay> findAll() {
        return essayRepository.findAll();
    }
}