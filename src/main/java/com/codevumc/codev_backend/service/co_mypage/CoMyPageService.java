package com.codevumc.codev_backend.service.co_mypage;

import com.codevumc.codev_backend.domain.CoPortfolio;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import org.json.simple.JSONArray;

public interface CoMyPageService {
    public void updateCoPortfolio(CoPortfolio coPortfolio, JSONArray co_portfoliolinksList, JSONArray co_portfolioLaguagesList);
}
