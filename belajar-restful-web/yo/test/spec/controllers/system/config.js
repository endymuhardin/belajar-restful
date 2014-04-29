'use strict';

describe('Controller: SystemConfigCtrl', function () {

  // load the controller's module
  beforeEach(module('yoApp'));

  var SystemConfigCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    SystemConfigCtrl = $controller('SystemConfigCtrl', {
      $scope: scope
    });
  }));

  it('should have proper test later on', function () {
  });
});
