package observer;

import dto.ApplicationDTO;

public interface ApplicationObserver {
    void onApplicationStatusChanged(ApplicationDTO application);
} 