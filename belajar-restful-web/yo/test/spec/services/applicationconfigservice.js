'use strict';

describe('Service: ApplicationConfigService', function () {

  // load the service's module
  beforeEach(module('yoApp'));

  // instantiate service
  var ApplicationConfigService;
  beforeEach(inject(function (_ApplicationConfigService_) {
    ApplicationConfigService = _ApplicationConfigService_;
  }));

  it('should do something', function () {
    expect(!!ApplicationConfigService).toBe(true);
  });

});
