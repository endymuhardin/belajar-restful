'use strict';

describe('Service: ApplicationSessionsService', function () {

  // load the service's module
  beforeEach(module('yoApp'));

  // instantiate service
  var ApplicationSessionsService;
  beforeEach(inject(function (_ApplicationSessionsService_) {
    ApplicationSessionsService = _ApplicationSessionsService_;
  }));

  it('should do something', function () {
    expect(!!ApplicationSessionsService).toBe(true);
  });

});
