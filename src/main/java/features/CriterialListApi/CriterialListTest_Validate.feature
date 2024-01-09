#Author: XuanPham
@CatApi
Feature: Criterial List Api Validate Test

  @ValidationCase
  Scenario Outline: Check validation of single field
    Given I have url and Method and request body of criterial list api
      | URL                                                               | RequestBodyName                                 |
      | http://dev-asmsbapi.seauat.com.vn/FT_LIMIT_DEST/rest/seab/process | CriterialListApi\\CriterialListRequestBody.json |
    When I sent criterial request with validation data with "<FieldName>" and "<Value>"
    Then I check <ExpectedStatusCode> and "<fieldExMessage>" and "<ExpectedMessage>" of criterial api correctly

    Examples: 
      | FieldName    | Value                 | ExpectedStatusCode | fieldExMessage | ExpectedMessage |
      | body.command | null                  |                200 | body.status    | FAILE           |
      | body.command | missing               |                200 | body.status    | FAILE           |
      | body.command | \\"\\"                |                200 | body.status    | FAILE           |
      | body.command | GET\\_TRANSACTIONTest |                200 | body.status    | FAILE           |
