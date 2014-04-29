'use strict';

describe('Controller: MenubarCtrl', function () {

  // load the controller's module
  beforeEach(module('yoApp'));

  var MenubarCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    MenubarCtrl = $controller('MenubarCtrl', {
      $scope: scope
    });
  }));

  it('should have userinfo with username endy', function () {
    expect(scope.userinfo.user).toBe('endy');
  });
});
