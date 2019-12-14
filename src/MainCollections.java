import com.urise.webapp.model.Resume;

import java.util.*;

public class MainCollections {
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final Resume RESUME_1 = new Resume(UUID_1);
    private static final Resume RESUME_2 = new Resume(UUID_2);
    private static final Resume RESUME_3 = new Resume(UUID_3);

    public static void main(String[] args) {
        Collection<Resume> collection = new ArrayList<>();
        collection.add(RESUME_1);
        collection.add(RESUME_2);
        collection.add(RESUME_3);

//        for (Resume resume : collection) {
//            System.out.println(resume);
//            if (Objects.equals(resume.getUuid(), UUID_1)) {
//                collection.remove(resume);
//            }
//        }
        Iterator<Resume> iterator = collection.iterator();
        while (iterator.hasNext()) {
            Resume resume = iterator.next();
            System.out.println(resume);
            if (Objects.equals(resume.getUuid(), UUID_1)) {
                iterator.remove();
            }
        }
        System.out.println(collection.toString());

        Map<String, Resume> map = new HashMap<String, Resume>() {
            {
                put("uuid1", RESUME_1);
                put("uuid2", RESUME_2);
                put("uuid3", RESUME_3);
            }
        };
    }
}
