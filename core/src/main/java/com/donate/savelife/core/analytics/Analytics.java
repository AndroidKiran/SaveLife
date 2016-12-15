package com.donate.savelife.core.analytics;

public interface Analytics {

    void trackSignInStarted(String method);

    void trackSignInSuccessful(String method);

    void trackSelectCountry(String country);

    void trackProfileSelection(String country);

    void trackSelectNeed(String userId, String needId);

    void trackMessageLength(int messageLength, String userId, String needId);

    void trackInvitationOpened(String senderId);

    void trackInvitationAccepted(String senderId);

    void trackSendInvitesSelected(String userId);

}
