#Author: Pham Xuan
@CatApi
Feature: Criterial List Api Main Test

  @MainCase
  Scenario Outline: Check response whe send request successfully
    Given I have url and Method and request body of criterial list api
      | URL                                                               | RequestBodyName                                 |
      | http://dev-asmsbapi.seauat.com.vn/FT_LIMIT_DEST/rest/seab/process | CriterialListApi\\CriterialListRequestBody.json |
    When I sent criterial list request
    Then I check <StatusCode> of criterial list api correctly

    Examples: 
      | URL                                                               | RequestBodyName                                 | StatusCode |
      | http://dev-asmsbapi.seauat.com.vn/FT_LIMIT_DEST/rest/seab/process | CriterialListApi\\CriterialListRequestBody.json |        200 |
