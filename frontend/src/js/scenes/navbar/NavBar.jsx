import React from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';

import Sidebar from 'grommet/components/Sidebar';
import Header from 'grommet/components/Header';
import Anchor from 'grommet/components/Anchor';
import Box from 'grommet/components/Box';
import Menu from 'grommet/components/Menu';
import Title from 'grommet/components/Title';
import Exit from 'grommet/components/icons/base/Logout';
import Footer from 'grommet/components/Footer';
import Button from 'grommet/components/Button';

import CarIcon from 'grommet/components/icons/base/Car';

import { logout } from '../login/data/login-actions';
import toggleNavBar from './data/navbar-actions';

const NavBar = (props) => {
  if (props.location.pathname === '/login') {
    return <div />;
  }

  return (
    <Sidebar
      colorIndex='neutral-1-a'
      fixed={false}
    >
      <Header
        pad={{ horizontal: 'small', between: 'small' }}
      >
        <Button onClick={props.toggleNavBar}>
          <CarIcon
            size='medium'
          />
        </Button>
        <Title>
          Collaborative Bookshelf
        </Title>
      </Header>
      <Box
        flex='grow'
        justify='start'
      >
        <Menu primary>
          <Anchor
            path={{ path: '/', index: true }}
          >
            Home
          </Anchor>
          <Anchor
            path={{ path: '/medicines', index: true }}
          >
            Medicines
          </Anchor>
        </Menu>
      </Box>
      {
        props.loginStore !== undefined ?
          <Footer pad='medium'>
            <Anchor
              onClick={props.logout}
              icon={<Exit />}
            >
              Logout
            </Anchor>
          </Footer> :
          <div />
      }
    </Sidebar>
  );
};

const mapStateToProps = state => ({
  loginStore: state.pages.login.store
});

export default connect(
  mapStateToProps,
  {
    logout,
    toggleNavBar
  },
)(NavBar);


NavBar.propTypes = {
  logout: PropTypes.func.isRequired,

  loginStore: PropTypes.shape({
    jwt: PropTypes.string,
  }).isRequired,

  location: PropTypes.shape({
    pathname: PropTypes.string,
  }).isRequired,

  toggleNavBar: PropTypes.func.isRequired,
};
