@test
Feature: test

  @Scenario1
  Scenario: 1.3	Story 1

    Then The "LME" instrument "PB_03_2018" with details
      |  last_trading_date     | delivery_date |  market           | label               |
      |  15-03-2018            | 17-03-2018    |  LME_PB           | Lead 13 March 2018  |


    When "LME" publishes instrument "PB_03_2018"

    Then Then the application publishes the following instrument internally with "PB_03_2018_TRADABLE"
      |  LAST_TRADING_DATE | DELIVERY_DATE  |  MARKET           | LABEL              | TRADABLE           |
      |  15-03-2018        | 17-03-2018     |  PB               | Lead 13 March 2018 |  TRUE              |

  @Scenario2
  Scenario: 1.4	Story 2


    Then The "LME" instrument "PB_03_2018" with details
      |  LAST_TRADING_DATE  | DELIVERY_DATE |  MARKET   | LABEL              |
      |  15-03-2018         | 17-03-2018    |  LME_PB   | Lead 13 March 2018 |

    And A "PRIME" instrument "PRIME_PB_03_2018" with these details
      |  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET      | LABEL              | EXCHANGE_CODE | TRADABLE|
      |  14-03-2018        | 18-03-2018    |  LME_PB      | Lead 13 March 2018 | PB_03_2018    | FALSE   |


    When "LME" publishes instrument "PB_03_2018"

    Then Then the application publishes the following instrument internally with "PB_03_2018_TRADABLE"
      |  LAST_TRADING_DATE  | DELIVERY_DATE |  MARKET | LABEL              |  TRADABLE      |
      |  15-03-2018         | 17-03-2018    |  PB     | Lead 13 March 2018 |  TRUE          |

    When "PRIME" publishes instrument "PB_03_2018"

    Then Then the application publishes the following instrument internally with "PB_03_2018_TRADABLE"
      |  LAST_TRADING_DATE  | DELIVERY_DATE |  MARKET | LABEL              |  TRADABLE      |
      |  15-03-2018         | 17-03-2018    |  PB     | Lead 13 March 2018 |  FALSE         |
