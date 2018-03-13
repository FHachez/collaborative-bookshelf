import React from 'react';
import { Route, Switch, Redirect, withRouter } from 'react-router-dom';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import Split from 'grommet/components/Split';
import Box from 'grommet/components/Box';

import Home from '../scenes/home/Home';
import Login from '../scenes/login/Login';
import NavBar from '../scenes/navbar/NavBar';
import Header from '../scenes/header/Header';
import Medicines from '../scenes/medicines/Medicines';

const NormalRoutes = props => (
  <Box>
    <Split flex='right'>
      {props.navBarStore.open ? <Route path='/' component={NavBar} /> : null}
      <Box style={{ height: '100%' }}>
        <Route path='/' component={Header} />
        <Switch>
          <Route exact path='/' component={Home} />
          <Route path='/medicines' component={Medicines} />
          <Route exact path='/login' component={Login} />
        </Switch>
      </Box>
    </Split>
  </Box>
);

const AuthRoutes = () => (
  <Switch>
    <Route exact path='/login' component={Login} />
    <Redirect to='/login' />
  </Switch>
);

const Routes = props => (
  props.loginStore.jwt ? <NormalRoutes navBarStore={props.navBarStore} /> : <AuthRoutes />
);


const mapStateToProps = state => ({
  loginStore: state.pages.login.store,
  navBarStore: state.pages.navBar.store
});

export default withRouter(
  connect(
    mapStateToProps,
  )(Routes),
);


Routes.propTypes = {
  loginStore: PropTypes.shape({
    jwt: PropTypes.string,
  }).isRequired,

  navBarStore: PropTypes.shape({
    open: PropTypes.boolean,
  }).isRequired,
};

NormalRoutes.propTypes = {
  navBarStore: PropTypes.shape({
    open: PropTypes.boolean,
  }).isRequired,
};
