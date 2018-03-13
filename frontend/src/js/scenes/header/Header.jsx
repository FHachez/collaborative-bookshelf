import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import GrommetHeader from 'grommet/components/Header';
import Box from 'grommet/components/Box';
import Title from 'grommet/components/Title';
import Button from 'grommet/components/Button';
import CarIcon from 'grommet/components/icons/base/Car';
import Timestamp from 'grommet/components/Timestamp';

import toggleNavBar from '../navbar/data/navbar-actions';

class Header extends Component {
  constructor() {
    super();

    this.state = {
      currentTime: new Date()
    };
  }

  componentDidMount = () => {
    this.setTimeInterval = setInterval(this.setTime, 1000);
  };

  componentWillUnmount = () => {
    clearInterval(this.setTimeInterval);
  };

  setTime = () => (
    this.setState({
      currentTime: new Date()
    })
  );

  render() {
    return (
      <Box
        pad='small'
        colorIndex='neutral-1-a'
      >
        <GrommetHeader
          size='small'
          float={false}
          fixed={false}
          splash={false}
        >
          {this.props.navBarStore.open ?
            null :
            <Button onClick={this.props.toggleNavBar}>
              <CarIcon
                size='medium'
              />
            </Button>
          }
          {this.props.navBarStore.open ?
            null :
            <Title>
              Collaborative Bookshelf
            </Title>
          }
          <Box
            flex
            justify='end'
            direction='row'
            responsive={false}
          >
            <Timestamp value={this.state.currentTime} />
          </Box>
        </GrommetHeader>
      </Box>
    );
  }
}


const mapStateToProps = state => ({
  navBarStore: state.pages.navBar.store
});

export default connect(
  mapStateToProps,
  {
    toggleNavBar
  },
)(Header);

Header.propTypes = {
  navBarStore: PropTypes.shape({
    open: PropTypes.boolean,
  }).isRequired,
  toggleNavBar: PropTypes.func.isRequired,
};
