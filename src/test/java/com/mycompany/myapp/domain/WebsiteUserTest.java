package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WebsiteUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WebsiteUser.class);
        WebsiteUser websiteUser1 = new WebsiteUser();
        websiteUser1.setId(1L);
        WebsiteUser websiteUser2 = new WebsiteUser();
        websiteUser2.setId(websiteUser1.getId());
        assertThat(websiteUser1).isEqualTo(websiteUser2);
        websiteUser2.setId(2L);
        assertThat(websiteUser1).isNotEqualTo(websiteUser2);
        websiteUser1.setId(null);
        assertThat(websiteUser1).isNotEqualTo(websiteUser2);
    }
}
