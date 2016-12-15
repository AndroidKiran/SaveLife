package com.donate.savelife.core.link;


import com.donate.savelife.core.user.data.model.User;

import java.net.URI;

public interface LinkFactory {

    URI inviteLinkFrom(User user);

}
