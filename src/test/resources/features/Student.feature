Feature: Student Operations

  Background:
    Given I use the "local" service

  Scenario: Get the first Student.
    When I send a "Get" request to "/student/1"
    Then I validate the response has status code 200
    And I validate the response contains:
      | id        | 1                                               |
      | firstName | Vernon                                          |
      | lastName  | Harper                                          |
      | email     | egestas.rhoncus.Proin@massaQuisqueporttitor.org |
      | programme | Financial Analysis                              |
      | courses   | [Accounting, Statistics]                        |

  Scenario: Create a new Student.
    When I send a "POST" request to "/student" with json body
    """
    {
    "firstName": "Jhasmany",
    "lastName": "Quiroz",
    "email":"jhasmany.quiroz@automation.org",
    "programme":"QE Automation",
    "courses":["UI Automation", "API Automation"]
    }
    """
    Then I validate the response has status code 201
    And I validate the response contains:
      | msg | Student added |

  Scenario: Updated a Student.
    Given I send a "POST" request to "/student" with json body
    """
    {
    "firstName": "Jhasmany",
    "lastName": "Quiroz",
    "email":"jhasmany.quiroz.test@automation.org",
    "programme":"QE Automation",
    "courses":["UI Automation", "API Automation"]
    }
    """
    And I validate the response has status code 201
    And I send a "Get" request to "/student/list"
    And I save the response as "students"
    When I obtained the Student by email "jhasmany.quiroz.test@automation.org" saved as "studentA"
    When I send a "PUT" request to "/student/{studentA.id}" with json body
    """
    {
    "firstName": "Jhasmany",
    "lastName": "Quiroz",
    "programme":"QE Automation",
    "email": "jhasmany.quiroz.updated@automation.org"
    }
    """
    Then I validate the response has status code 200
    And I validate the response contains:
      | msg | Student Updated |